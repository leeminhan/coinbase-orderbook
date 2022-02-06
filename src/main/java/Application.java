import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ws.WebSocketClient;

import java.util.concurrent.CountDownLatch;

public class Application {
    private static final Logger LOG = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(countDownLatch::countDown));

        String instrument = Application.parseCommandLineArgs(args);
        WebSocketClient wsc = new WebSocketClient();
        wsc.start(instrument);

        try {
            countDownLatch.await();
            wsc.end();
        } catch (InterruptedException e) {
            LOG.error(e);
        }

        LOG.info("EXITING PROGRAM\n");
    }

    public static String parseCommandLineArgs(String[] args) {
        if (args.length == 0) {
            LOG.error("Kindly provide Instrument such as BTC-USD as CommandLine Argument");
            System.exit(1);
        }
        LOG.info("STARTING PROGRAM: Fetching Coinbase OrderBook for {}", args[0]);
        return args[0];
    }
}
