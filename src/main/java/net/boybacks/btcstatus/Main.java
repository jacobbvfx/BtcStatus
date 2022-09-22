package net.boybacks.btcstatus;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;

import static java.awt.Color.*;

public class Main implements ActionListener {

    public static void main(String[] args) throws IOException {
        FileManager();
        createUI();
        systemTray();
    }

    static float BtcPrice = 0;
    static String Coin = "BTC";
    static String Currency = "PLN";

    static float _width = 450;
    static float _height = 550;

    static JFrame frame = new JFrame();

    static JPanel Uppersection = new JPanel();
    static JLabel Text = new JLabel(VariablesManager.MAINTITLETEXT.getValue());
    static JButton Exit = new JButton(VariablesManager.EXITBUTTONTEXT.getValue());

    static JPanel Middlesection = new JPanel();
    static JLabel MiddleText = new JLabel("Click refresh", SwingConstants.CENTER);

    static JPanel Bottomsection = new JPanel();
    static JButton Refresh = new JButton(VariablesManager.REFRESHBUTTONTEXT.getValue());

    public static void createUI() throws IOException {
        InputStream inputStream = null;
        Font font = new Font(null);

        try {
            inputStream = Main.class.getClassLoader().getResourceAsStream("Panton-BlackCaps.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }

        MoveListener listener = new MoveListener();
        frame.setTitle("BTCStatus");
        //ImageIcon icon = new ImageIcon(Main.class.getClassLoader().getResource("btcstatus.png"));
        frame.setIconImage(new ImageIcon(Main.class.getClassLoader().getResource("btcstatus.png")).getImage());
        frame.addMouseListener(listener);
        frame.addMouseMotionListener(listener);
        frame.setUndecorated(true);
        frame.add(Uppersection, BorderLayout.BEFORE_FIRST_LINE);
        frame.add(Middlesection, BorderLayout.CENTER);
        frame.add(Bottomsection, BorderLayout.PAGE_END);
        frame.setSize((int) _width, (int) _height);
        frame.setShape(new RoundRectangle2D.Double(0, 0, _width, _height, 20, 20));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int) ((screenSize.width/2 ) - _width/2), (int) ((screenSize.height/2) - _height/2));
        frame.setAlwaysOnTop(true);
        frame.getContentPane().setBackground(Color.decode(VariablesManager.LIGHT_GREY.getValue()));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(VariablesManager.MAINTITLETEXT.getValue());
        frame.setResizable(false);

        Uppersection.setBackground(Color.decode(VariablesManager.DARK_GREEN.getValue()));
        Uppersection.add(Text);
        Uppersection.add(Exit);

        Exit.setFont(font.deriveFont(Font.PLAIN, 20));
        Exit.setBackground(Color.decode(VariablesManager.BROWN.getValue()));
        Exit.setForeground(WHITE);
        Exit.addActionListener(new CloseListener());

        Text.setFont(font.deriveFont(Font.PLAIN, 38));
        Text.setForeground(white);

        Bottomsection.setBackground(null);
        Bottomsection.add(Refresh, Component.CENTER_ALIGNMENT);

        Refresh.setFont(font.deriveFont(Font.PLAIN, 48));
        Refresh.setBackground(Color.decode(VariablesManager.DARK_GREEN.getValue()));
        Refresh.setForeground(WHITE);
        Refresh.addActionListener(new RefreshListener());

        Middlesection.setBackground(null);
        Middlesection.setLayout(new GridLayout(1,1));
        Middlesection.add(MiddleText);

        MiddleText.setFont(font.deriveFont(Font.PLAIN, 32));
        MiddleText.setBackground(Color.decode(VariablesManager.DARK_GREEN.getValue()));
        MiddleText.setForeground(WHITE);
    }

    static Properties prop = new Properties();
    public static void FileManager() {
        File f = new File("config.properties");
        if(f.exists() && !f.isDirectory()) {
            System.out.println("[Test Log (File Creation)] File is already been created!");
        }
        else {
            try {
                prop.setProperty("coin", "BTC");
                prop.setProperty("currency", "PLN");
                //prop.setProperty("auto-refresh", "false");

                prop.store(new FileOutputStream("config.properties"), null);
                System.out.println("[Test Log (File Creation)] File has been created!");

            } catch (IOException ex) {
                System.out.println("[Test Log (File Creation)] Ooooooooooops Error...");
                ex.printStackTrace();
            }
        }
        Properties properties = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config.properties");
        } catch (FileNotFoundException e) {
            System.out.println("[Test Log (File Read)] Ooooooooooops Error there is no such file config.properties...");
            e.printStackTrace();
        }
        try {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("[Test Log (File Read)] Ooooooooooops Error with input...");
            e.printStackTrace();
        }
        Coin = (String) properties.get("coin");
        Currency = (String) properties.get("currency");
        //Auto = (String) properties.get("auto-refresh");

        System.out.println("[Test Log (File Read)]" + Coin + "|" + Currency + "|");
    }

    public static void systemTray() {
        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported !!! ");
            return ;
        }
        SystemTray systemTray = SystemTray.getSystemTray();

        Image image = new ImageIcon(Main.class.getClassLoader().getResource("btcstatus.png")).getImage();

        PopupMenu trayPopupMenu = new PopupMenu();

        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);

        TrayIcon trayIcon = new TrayIcon(image, "BTCStats", trayPopupMenu);
        trayIcon.setImageAutoSize(true);

        try{
            systemTray.add(trayIcon);
        }catch(AWTException awtException){
            awtException.printStackTrace();
        }
    }

    public static void priceChecker(String COIN, String PRICE) {

        try {
            URL url = new URL("https://api.zonda.exchange/rest/trading/ticker/" + COIN.toUpperCase() + "-" + PRICE.toUpperCase());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getInstanceFollowRedirects()) {

            }
            conn.connect();

            int responsecode = conn.getResponseCode();

            if (responsecode != 200)
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            else {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                scanner.close();

                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);


                JSONObject obj = (JSONObject) data_obj.get("ticker");
                if (obj == null) {
                    MiddleText.setText("There is problem with save file!");
                    return;
                }

                String Price = String.valueOf(obj.get("rate"));
                BtcPrice = Float.parseFloat(Price);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        MiddleText.setText(VariablesManager.BTCPRRICECOINSTEXT.getValue().replace("[0]", String.valueOf(BtcPrice) + " " + PRICE).replace("[1]", "1 " + COIN));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static class MoveListener implements MouseListener, MouseMotionListener {

        private Point pressedPoint;
        private Rectangle frameBounds;

        @Override
        public void mouseClicked(MouseEvent event) {
        }

        @Override
        public void mousePressed(MouseEvent event) {
            this.frameBounds = frame.getBounds();
            this.pressedPoint = event.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            moveJFrame(event);
        }

        @Override
        public void mouseEntered(MouseEvent event) {
        }

        @Override
        public void mouseExited(MouseEvent event) {
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            moveJFrame(event);
        }

        @Override
        public void mouseMoved(MouseEvent event) {
        }

        private void moveJFrame(MouseEvent event) {
            Point endPoint = event.getPoint();

            int xDiff = endPoint.x - pressedPoint.x;
            int yDiff = endPoint.y - pressedPoint.y;
            frameBounds.x += xDiff;
            frameBounds.y += yDiff;
            frame.setBounds(frameBounds);
        }
    }

    private static class CloseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    private static class RefreshListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            priceChecker(Coin, Currency);
        }
    }
}
