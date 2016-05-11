package JavaUtils.DownloadManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Let's you easily download a File
 * 
 * @author Max
 * @see JavaUtils
 */
public class JavaDownloader {

	/**
	 * Downloads a File from the URL url in the File target with an Optional
	 * DownloadListener dl Recommended with DownloadListener
	 * 
	 * @param url
	 *            The URL of the downloading File
	 * @param target
	 *            The Target File
	 * @param dl
	 *            An Optional {@link DownloadListener} (Can be null)
	 * @throws IOException
	 *             Occures when the Host can't be found
	 */
	public static void downloadFile(URL url, File target, DownloadListener dl)
			throws IOException {
		if (!target.exists()) {
			if (target.getPath().contains("\\")) {
				new File(target.getPath().substring(0,
						target.getPath().lastIndexOf("\\"))).mkdirs();
			}
			target.createNewFile();
		}
		final URLConnection conn = url.openConnection();
		String l = conn.getHeaderField("Content-Length");
		long length = 0;
		if (l == null)
			l = "1";
		if (!l.isEmpty()) {
			length = Integer.valueOf(l);
		}
		if (dl != null)
			dl.startedDownloading(target.getName(), length);
		final InputStream is = new BufferedInputStream(conn.getInputStream());
		final OutputStream os = new BufferedOutputStream(new FileOutputStream(
				target));
		byte[] chunk = new byte[1024];
		int chunkSize;
		while ((chunkSize = is.read(chunk)) != -1) {
			os.write(chunk, 0, chunkSize);
			if (dl != null)
				dl.downloadTileFile(target.getName(), chunkSize);
		}
		os.flush(); // Necessary for Java < 6
		os.close();
		is.close();
		if (dl != null)
			dl.downloadedFile(target.getName());
	}

	/**
	 * Downloads a File from the URL url in the File target with an Optional
	 * DownloadListener dl Recommended with DownloadListener
	 * 
	 * @see JavaDownloader#downloadFile(URL, File, DownloadListener)
	 * @param url
	 *            The URL of the downloading File
	 * @param target
	 *            The Target File
	 * @param dl
	 *            An Optional {@link DownloadListener} (Can be null)
	 * @throws IOException
	 *             Occures when the Host can't be found
	 */
	@Deprecated
	public static void downloadFile(URL url, File target) throws IOException {
		downloadFile(url, target, null);
	}

	/**
	 * Gets the Size of an URL File
	 * 
	 * @param url
	 *            The URL of the File
	 * @return The Length of the File
	 * @throws IOException
	 *             Occures when the Host can't be found
	 */
	public static int getContentLength(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		return conn.getHeaderFieldInt("Content-Length", 0);
	}

	/**
	 * Downloaded eine Datei Asynchron von der URL url in Die File target
	 * Recommended with DownloadListener
	 * 
	 * @param url
	 *            Die Url Der heruterzuladenden Datei
	 * @param target
	 *            Die Datei in die geschrieben werden soll
	 */
	@Deprecated
	public static void downloadFileAsynchronously(final URL url,
			final File target) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					downloadFile(url, target, null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Downloaded eine Datei Asynchron von der URL url in Die File target mit
	 * einem optionalem DownloadListener dl
	 * 
	 * @param url
	 *            Die Url Der heruterzuladenden Datei
	 * @param target
	 *            Die Datei in die geschrieben werden soll
	 * @param dl
	 *            Ein Optionaler DownloadListener (kann auch null sein)
	 */
	public static void downloadFileAsynchronously(final URL url,
			final File target, final DownloadListener dl) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					downloadFile(url, target, dl);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}


}
