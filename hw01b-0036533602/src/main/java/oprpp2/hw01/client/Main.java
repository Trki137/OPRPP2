package oprpp2.hw01.client;

import oprpp2.hw01.message.InMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.*;

/**
 * This class represents GUI for client application
 */
public class Main extends JFrame {
    /**
     * Instance of {@link Client}
     */
    private final Client client;
    /**
     * User input field
     */
    private final JTextField textField = new JTextField();
    /**
     * Text field to show messages
     */
    private final JTextArea textArea = new JTextArea();

    /**
     * Creates instance of {@link Main} and starts communication with server and starts GUI
     * @param client instance of {@link Client}
     * @param hostName address of server
     * @param username client name
     * @param port which port does server uses
     */

    public Main(Client client, String hostName, String username, int port) {
        this.client = client;
        setTitle("Chat client: " + username);

        WindowListener wl = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.disconnectFromServer();
                dispose();
            }
        };

        addWindowListener(wl);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        client.listenForMessages(message -> {
            InMessage inMessage = (InMessage) message;
            StringBuilder sb = new StringBuilder();
            try {
                sb.append("[").append(InetAddress.getByName(hostName)).append(":").append(port).append("]");
                sb.append("Poruka od korisnika: ").append(inMessage.getName()).append("\n");
                sb.append(inMessage.getMessage()).append("\n");
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
            textArea.append(sb.toString());
        });

        setSize(new Dimension(300, 500));
        initGui();
        setLocationRelativeTo(null);
        setVisible(true);


    }

    /**
     * Initializes actions for GUI elements
     */
    private void initActions() {
        textField.setAction(sendOnEnter);
    }

    /**
     * Initializes GUI layout and appearance
     */

    private void initGui() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(textField, BorderLayout.PAGE_START);
        container.add(textArea, BorderLayout.CENTER);

        initActions();
    }

    /**
     * Action for text field when enter is pressed
     */

    private final Action sendOnEnter = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String message = textField.getText();
            try {
                client.sendMessage(message);
            } catch (UnknownHostException ignored) {
            }
            textField.setText("");
        }
    };

    /**
     * Starting point of our application for Client
     * @param args connection parameters
     * @throws SocketException
     * @throws UnknownHostException
     * @throws IllegalArgumentException if args arguments is invalid. Expects address of server, port of server and username for client
     */
    public static void main(String[] args) throws SocketException, UnknownHostException {
        if (args.length != 3) {
            throw new IllegalArgumentException(String.format("Expected 3 argument but got  %d.\n Expected format is IP ADDRESS OF SERVER, PORT SERVER, NAME OF USER", args.length));
        }
        String hostName = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        Client client = new Client(port, hostName, username);

        boolean success = client.connectToServer();

        if (!success) {
            System.out.println("Couldn't connect to server");
            return;
        }

        SwingUtilities.invokeLater(() -> new Main(client, hostName, username, port));

    }
}

