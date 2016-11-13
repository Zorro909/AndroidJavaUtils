package JavaUtils.Android.WiFi.Direct;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.HashMap;

import JavaUtils.Android.AndroidCheck;
import JavaUtils.Android.AndroidUtils;
import JavaUtils.Android.NotEnoughPermissionsException;
import JavaUtils.TCPManager.TCPManager;
import JavaUtils.TCPManager.XmlTcpConnection;
import android.Manifest.permission;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class DirectWifiManager extends BroadcastReceiver {
	IntentFilter filter;
	Channel mChannel;
	WifiP2pManager mManager;
	int port;
	WifiP2pDeviceList allPeers;
	HashMap<String, WifiP2pDevice> servers;
	boolean lookForServer = true;

	public DirectWifiManager(Activity ac, int port)
			throws NotEnoughPermissionsException {
		this.port = port;
		AndroidCheck a = AndroidCheck.getInstance(ac);
		if (a.hasPermissions(permission.INTERNET, permission.ACCESS_WIFI_STATE,
				permission.CHANGE_WIFI_STATE)) {
			if (a.checkVersion(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
				setup(ac);
			} else {
				AndroidUtils.showPopup(
						ac,
						"Android Version Error",
						"Android System Version is too low! ("
								+ Build.VERSION_CODES.ICE_CREAM_SANDWICH
								+ " Needed)", null, "OK", null).getClicked();
			}
		} else {
			AndroidUtils.showPopup(ac, "Permission Error",
					"Not enough Permissions granted!", null, "OK", null)
					.getClicked();
			throw new NotEnoughPermissionsException(permission.INTERNET + ", "
					+ permission.ACCESS_WIFI_STATE + ", "
					+ permission.CHANGE_WIFI_STATE);
		}
	}

	public DirectWifiManager(Activity ac, int port, boolean lookForServer)
			throws NotEnoughPermissionsException {
		this.port = port;
		this.lookForServer = lookForServer;
		AndroidCheck a = AndroidCheck.getInstance(ac);
		if (a.hasPermissions(permission.INTERNET, permission.ACCESS_WIFI_STATE,
				permission.CHANGE_WIFI_STATE)) {
			if (a.checkVersion(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
				setup(ac);
			} else {
				AndroidUtils.showPopup(
						ac,
						"Android Version Error",
						"Android System Version is too low! ("
								+ Build.VERSION_CODES.ICE_CREAM_SANDWICH
								+ " Needed)", null, "OK", null).getClicked();
			}
		} else {
			AndroidUtils.showPopup(ac, "Permission Error",
					"Not enough Permissions granted!", null, "OK", null)
					.getClicked();
			throw new NotEnoughPermissionsException(permission.INTERNET + ", "
					+ permission.ACCESS_WIFI_STATE + ", "
					+ permission.CHANGE_WIFI_STATE);
		}
	}

	public void createGroup() {
		mManager.createGroup(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
			}

			@Override
			public void onFailure(int reason) {
			}

		});
	}

	private void setup(Activity a) {
		WifiManager wifiManager = (WifiManager) a.getSystemService("wifi");
		wifiManager.setWifiEnabled(true);

		mManager = (WifiP2pManager) a
				.getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(a, a.getMainLooper(), null);

		Method m;
		try {
			m = mManager.getClass().getMethod("enableP2p", Channel.class);
			m.invoke(mManager, mChannel);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		filter = new IntentFilter();
		filter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		filter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		filter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		filter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}

	public IntentFilter getIntentFilter() {
		return filter;
	}

	public void discoverPeers() {
		mManager.discoverPeers(mChannel, new ActionListener() {

			@Override
			public void onSuccess() {
			}

			@Override
			public void onFailure(int reason) {
			}

		});
	}

	public WifiP2pDeviceList getAllDevices() {
		return allPeers;
	}

	public HashMap<String, WifiP2pDevice> getAllServerDevices()
			throws InterruptedException {
		while (servers == null)
			Thread.sleep(50L);
		return servers;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

			} else {

			}
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			if (mManager != null) {
				servers = new HashMap<String, WifiP2pDevice>();
				mManager.requestPeers(mChannel, new PeerListListener() {

					@Override
					public void onPeersAvailable(WifiP2pDeviceList peers) {
						allPeers = peers;
						for (final WifiP2pDevice device : peers.getDeviceList()) {
							final WifiP2pConfig config = new WifiP2pConfig();
							config.deviceAddress = device.deviceAddress;
							final String name = device.deviceName;
							if (device.isGroupOwner()) {
								mManager.connect(mChannel, config,
										new ActionListener() {

											@Override
											public void onSuccess() {
												if (lookForServer) {
													mManager.requestConnectionInfo(
															mChannel,
															new WifiP2pManager.ConnectionInfoListener() {
																@Override
																public void onConnectionInfoAvailable(
																		WifiP2pInfo wifiP2pInfo) {
																	InetAddress address = wifiP2pInfo.groupOwnerAddress;
																	try {
																		XmlTcpConnection xml = TCPManager
																				.connectXml(
																						address.getHostAddress(),
																						port,
																						false,
																						null);
																		xml.disconnect();
																		servers.put(
																				name,
																				device);
																	} catch (Exception e) {
																		mManager.cancelConnect(
																				mChannel,
																				new ActionListener() {
																					@Override
																					public void onSuccess() {
																					}

																					@Override
																					public void onFailure(
																							int reason) {
																					}
																				});
																	}
																}
															});
												}
											}

											@Override
											public void onFailure(int reason) {
											}

										});
							}
						}
					}

				});
			}
		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
				.equals(action)) {

		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
				.equals(action)) {

		}

	}

}
