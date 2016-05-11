package JavaUtils.DownloadManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JTextField;

import JavaUtils.HTTPManager.InetManager;

public class JUpdater {

	private static String UPDATING_INSTALLING = "Update wird installiert";
	
	/**
	 * @param url
	 * @param location
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws InterruptedException
	 */
	public static void autoUpdate(String updateURL, Class mainClass,
			double currentVersion) throws MalformedURLException, IOException,
			InterruptedException {
		if(InetManager.openConnection(updateURL + "/updateData.php?checkVersion=1&version=" + currentVersion).initGet(false, new HashMap<String,String>()).get().contains("Newer")){
		JavaDownloader.downloadFile(new URL(updateURL + "/updateData.php?version=" + currentVersion),
				new File("updateData"), null);
		File f = new File("updateData");
		BufferedReader br = new BufferedReader(new FileReader(f));
		while (!br.readLine().contains("UpdateData")) {
		}
		if (Double.valueOf(br.readLine()) > currentVersion) {
			String updaterFile = mainClass.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			String l = "\\";
			if (updaterFile.contains("/")) {
				l = "/";
			}
			updaterFile = updaterFile.substring(0, updaterFile.lastIndexOf(l));
			JFrame frame = new JFrame();
			JTextField field = new JTextField(UPDATING_INSTALLING + " 0%");
			field.setEditable(false);
			frame.add(field);
			frame.setBounds(900, 400, 200, 100);
			frame.setVisible(true);
			String input;
			ArrayList<String> files = new ArrayList<String>();
			while ((input = br.readLine()) != null)
				files.add(input);
			int index = 0;
			for (String s : files) {
				index++;
				String url = s.split(":")[0];
				String path = s.split(":")[1];
				if (path.startsWith("\"")) {
					if (path.startsWith("\"\"")) {
						JavaDownloader.downloadFile(new URL(updateURL + "/" + url), new File(
								path));
						if (path.endsWith(".jar") || path.endsWith(".exe"))
							new File(path).setExecutable(true);
					} else {
                        String updater = System.getProperty(path.substring(1).split("\"")[0]) + path.substring(1).split("\"")[1];
                        JavaDownloader.downloadFile(new URL(updateURL + "/" + url), new File(
								updater + "/" + path));
						if (path.endsWith(".jar") || path.endsWith(".exe"))
							new File(path).setExecutable(true);
					}
				} else {
					JavaDownloader.downloadFile(new URL(updateURL + "/" + url), new File(
							updaterFile + "/" + path));
					if (path.endsWith(".jar") || path.endsWith(".exe"))
						new File(updaterFile + "/" + path).setExecutable(true);
				}
				field.setText("Update wird installiert " + index
						* (100 / files.size()) + "%");
			}
			field.setText("Das Programm wird nun neugestartet!");
			br.close();
			f.delete();
			try {
				Thread.sleep(1000L);
				field.setVisible(false);
				restartApplication(mainClass);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		br.close();
		f.delete();
		}
	}

	public static void restartApplication(Class c) throws URISyntaxException,
			IOException {
		final String javaBin = System.getProperty("java.home") + File.separator
				+ "bin" + File.separator + "java";
		final File currentJar = new File(c.getProtectionDomain()
				.getCodeSource().getLocation().toURI());

		/* is it a jar file? */
		if (!currentJar.getName().endsWith(".jar"))
			return;

		/* Build command: java -jar application.jar */
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
		System.exit(0);
	}
	
}
