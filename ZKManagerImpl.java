package hello;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ZKManagerImpl implements ZKManager {
    private static ZooKeeper zkeeper;
    private static ZKConnection zkConnection;

    public ZKManagerImpl() {
        initialize();
    }

    private void initialize() {
        zkConnection = new ZKConnection();
        try {
            zkeeper = zkConnection.connect("192.168.101.2");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            zkConnection.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void create(String path, byte[] data)
            throws KeeperException,
            InterruptedException {

        zkeeper.create(
                path,
                data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
    }

    public Object getZNodeData(String path, boolean watchFlag) {byte[] b = null;
        try {
            b = zkeeper.getData(path, null, null);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return new String(b, StandardCharsets.UTF_8);
    }

    public void update(String path, byte[] data) throws KeeperException,
            InterruptedException {
        int version = zkeeper.exists(path, true).getVersion();
        zkeeper.setData(path, data,  version);
    }
}