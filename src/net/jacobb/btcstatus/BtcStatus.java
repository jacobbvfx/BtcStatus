package net.jacobb.btcstatus;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.awt.Color.*;

public class BtcStatus implements ActionListener {

    Color greenish = Color.decode("#009846");

    String BtcPrice = null;
    String Name = "BTCSTATUS";

    JFrame f = new JFrame();
    JPanel h = new JPanel();
    JPanel r = new JPanel();
    JPanel d = new JPanel();
    JLabel t = new JLabel(Name);
    JLabel l = new JLabel("Click to check the Rate", SwingConstants.CENTER);
    JButton b = new JButton("REFRESH");

    Date date = new Date();
    SimpleDateFormat fm = new SimpleDateFormat("k:mm");

    Calendar dark = Calendar.getInstance();

    //Auto Dark/Light Mode W.I.P
    public void DateSetter() {

        dark.set(Calendar.HOUR, 18);
        dark.set(Calendar.MINUTE, 0);
        dark.set(Calendar.SECOND, 0);
        dark.set(Calendar.MILLISECOND, 0);

        //date system screwed
        //date =/ calendar

        System.out.println("Date Test: " + fm.format(dark));

        if (dark == null) {
            System.out.println("test");
        }

    }
// Main Function
    BtcStatus() {

        //DateSetter();

        f.add(h, BorderLayout.BEFORE_FIRST_LINE);
        f.add(r, BorderLayout.CENTER);
        f.add(d, BorderLayout.PAGE_END);
        f.setSize(415, 530);
        f.getContentPane().setBackground(gray);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle(Name);
        f.setResizable(false);

        h.setBackground(RED);
        h.add(t);

        r.setBackground(BLACK);
        r.setLayout(new GridBagLayout());
        r.add(l);

        d.setBackground(BLACK);
        d.add(b);

        t.setFont(new Font("Bebas", Font.BOLD,32));
        t.setForeground(white);

        l.setFont(new Font("Bebas", Font.BOLD,36));
        l.setForeground(white);

        b.setFont(new Font("Bebas", Font.BOLD,28));
        b.setBackground(greenish);
        b.setForeground(white);
        b.setOpaque(true);
        b.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String JsonUrl = "https://bitbay.net/API/Public/BTCPLN/ticker.json";

        URL url = null;
        try {
            url = new URL(JsonUrl);
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        }
        URLConnection request = null;
        try {
            request = url.openConnection();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try {
            request.connect();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        JsonParser jp = new JsonParser();
        JsonElement root = null;
        try {
            root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        JsonObject rootobj = root.getAsJsonObject();
        BtcPrice = rootobj.get("average").getAsString();
        System.out.println("[Test Log] " + BtcPrice + " ZL");
        l.setText("<html>Rate BTC/PLN:<br/>" + BtcPrice + " ZL" + "</html>");
    }

    public static void main(String[] args) {

            BtcStatus bs = new BtcStatus();

    }

}