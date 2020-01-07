/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.afm_wiki.utility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Driver;
import java.sql.DriverManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.RandomUtils;

import snaq.db.ConnectionPool;

/**
 * Class for utility methods and functions.
 *
 * @author Murat Artim
 * @date 27 Feb 2017
 * @time 13:36:47
 */
public class Utility {

	/** Result line length. */
	private static int RESULT_LINE_LENGTH = 90;

	/** Downloads directory. */
	// public static final Path DOWNLOADS_DIR = Paths.get("C:\\Users\\ts87am\\Documents\\Equinox\\downloads");
	public static final Path DOWNLOADS_DIR = Paths.get("/Users/aurora/Documents/Developer/EclipseWorkspace/afm-wiki/downloads");

	/**
	 * Shuts down the given thread executor in two phases, first by calling shutdown to reject incoming tasks, and then calling shutdownNow, if necessary, to cancel any lingering tasks.
	 *
	 * @param executor
	 *            Thread executor to shutdown.
	 */
	public static void shutdownThreadExecutor(ExecutorService executor) {

		// disable new tasks from being submitted
		executor.shutdown();

		try {

			// wait a while for existing tasks to terminate
			if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {

				// cancel currently executing tasks
				executor.shutdownNow();

				// wait a while for tasks to respond to being canceled
				if (!executor.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Thread pool " + executor.toString() + " did not terminate.");
			}
		}

		// exception occurred during shutting down the thread pool
		catch (InterruptedException ie) {

			// cancel if current thread also interrupted
			executor.shutdownNow();

			// preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Sets up database connection pool for connecting to global database.
	 *
	 * @param hostname
	 *            Host name of the database server.
	 * @param port
	 *            Port number of the database server.
	 * @param path
	 *            Path to global database.
	 * @param username
	 *            Database username.
	 * @param password
	 *            Database password.
	 * @return The newly created database connection pool.
	 */
	public static ConnectionPool setupDatabaseConnectionPool(String hostname, String port, String path, String username, String password) {

		try {

			// register database driver
			Class<?> c = Class.forName("org.apache.derby.jdbc.ClientDriver");
			Driver driver = (Driver) c.newInstance();
			DriverManager.registerDriver(driver);

			// get database properties of server
			String poolName = "Global DCP";
			int minPool = 1;
			int maxPool = 10;
			int maxSize = 0;
			int idleTimeout = 180000;
			String dbURL = "jdbc:derby://" + hostname + ":" + port + "/" + path;

			// create and initialize database connection pool
			ConnectionPool dbPool = new ConnectionPool(poolName, minPool, maxPool, maxSize, idleTimeout, dbURL, username, password);

			// register shut down hook (to ensure it releases resources when JVM exits)
			dbPool.registerShutdownHook();

			// return pool
			return dbPool;
		}

		// exception occurred during process
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Trims and returns result line.
	 *
	 * @param line
	 *            Result line to trim.
	 * @return The result line.
	 */
	public static String trimResultLine(String line) {
		return line.length() > RESULT_LINE_LENGTH ? line.substring(0, RESULT_LINE_LENGTH - 3) + "..." : line;
	}

	/**
	 * Returns human readable file size.
	 *
	 * @param size
	 *            Size in bytes.
	 * @return Human readable file size.
	 */
	public static String readableFileSize(long size) {
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "bytes", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/**
	 * Checks and corrects (if necessary) the given file name to be valid for Windows OS.
	 *
	 * @param fileName
	 *            File name to be checked.
	 * @return Modified file name.
	 */
	public static String correctFileName(String fileName) {
		if (fileName.contains("\\"))
			fileName = fileName.replaceAll(Matcher.quoteReplacement("\\"), "_");
		if (fileName.contains("$"))
			fileName = fileName.replaceAll(Matcher.quoteReplacement("$"), "_");
		if (fileName.contains("/"))
			fileName = fileName.replaceAll("/", "_");
		if (fileName.contains(":"))
			fileName = fileName.replaceAll(":", "_");
		if (fileName.contains("*"))
			fileName = fileName.replaceAll("\\*", "_");
		if (fileName.contains("?"))
			fileName = fileName.replaceAll("\\?", "_");
		if (fileName.contains("\""))
			fileName = fileName.replaceAll("\"", "_");
		if (fileName.contains("<"))
			fileName = fileName.replaceAll("<", "_");
		if (fileName.contains(">"))
			fileName = fileName.replaceAll(">", "_");
		if (fileName.contains("|"))
			fileName = fileName.replaceAll("\\|", "_");
		return fileName;
	}

	/**
	 * Creates and returns a download file path.
	 *
	 * @return Newly created download file path.
	 */
	public static Path createDownloadFilePath() {
		Path path = DOWNLOADS_DIR.resolve("download_" + RandomUtils.nextInt(0, 10000) + ".zip");
		while (Files.exists(path))
			path = DOWNLOADS_DIR.resolve("download_" + RandomUtils.nextInt(0, 10000) + ".zip");
		return path;
	}

	/**
	 * Deletes given file recursively.
	 *
	 * @param path
	 *            Path to directory where temporary files are kept.
	 * @param keep
	 *            Files to keep.
	 */
	public static void deleteTemporaryFiles(Path path, Path... keep) {

		try {

			// null path
			if (path == null)
				return;

			// directory
			if (Files.isDirectory(path)) {

				// create directory stream
				try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {

					// get iterator
					Iterator<Path> iterator = dirStream.iterator();

					// loop over files
					while (iterator.hasNext())
						deleteTemporaryFiles(iterator.next(), keep);
				}

				// delete directory (if not to be kept)
				try {
					if (!containsFile(path, keep))
						Files.delete(path);
				}

				// directory not empty
				catch (DirectoryNotEmptyException e) {
					// ignore
				}
			}
			else if (!containsFile(path, keep))
				Files.delete(path);
		}

		// exception occurred during process
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns true if given target file is contained within the given files array.
	 *
	 * @param target
	 *            Target file to search for.
	 * @param files
	 *            Array of files to search the target file.
	 * @return True if given target file is contained within the given files array.
	 */
	public static boolean containsFile(Path target, Path... files) {
		for (Path file : files)
			if (file.equals(target))
				return true;
		return false;
	}

	/**
	 * Zips given files to given output file.
	 *
	 * @param files
	 *            Files to zip.
	 * @param output
	 *            Output file path.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	public static void zipFiles(ArrayList<Path> files, File output) throws Exception {

		// create zip output stream
		try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(output)))) {

			// create buffer to store to be written bytes
			byte[] buf = new byte[1024];

			// loop over input files
			for (int i = 0; i < files.size(); i++) {

				// get file
				Path file = files.get(i);

				// get file name
				Path fileName = file.getFileName();
				if (fileName == null)
					throw new Exception("Cannot get file name.");

				// zip file
				zipFile(file, fileName.toString(), zos, buf);
			}
		}
	}

	/**
	 * Zips given file recursively.
	 *
	 * @param path
	 *            Path to file.
	 * @param name
	 *            Name of file.
	 * @param zos
	 *            Zip output stream.
	 * @param buf
	 *            Byte buffer.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void zipFile(Path path, String name, ZipOutputStream zos, byte[] buf) throws Exception {

		// directory
		if (Files.isDirectory(path)) {

			// create and close new zip entry
			zos.putNextEntry(new ZipEntry(name + "/"));
			zos.closeEntry();

			// create directory stream
			try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {

				// get iterator
				Iterator<Path> iterator = dirStream.iterator();

				// loop over files
				while (iterator.hasNext()) {

					// get file
					Path file = iterator.next();

					// get file name
					Path fileName = file.getFileName();
					if (fileName == null)
						throw new Exception("Cannot get file name.");

					// zip file
					zipFile(file, name + "/" + fileName.toString(), zos, buf);
				}
			}
		}

		// file
		else {

			// create new zip entry
			zos.putNextEntry(new ZipEntry(name));

			// create stream to read file
			try (FileInputStream fis = new FileInputStream(path.toString())) {

				// read till the end of file
				int len;
				while ((len = fis.read(buf)) > 0)
					zos.write(buf, 0, len);
			}

			// close zip entry
			zos.closeEntry();
		}
	}
}
