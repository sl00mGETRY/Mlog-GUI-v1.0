import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;
import org.fife.ui.autocomplete.*;

public class Main extends JFrame {
    private Properties lang = new Properties();
    private JMenu langMenu;

    public Main() {
        loadLanguage("en");

        JPanel cp = new JPanel(new BorderLayout());

        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);

        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        String[] mlogCommands = {"read", "write", "draw", "print", "drawflush", "printflush", 
                                 "getlink", "control", "sensor", "op", "end", "jump", 
                                 "unitbind", "unitcontrol", "unitradar", "unitlocate"};
        for (String cmd : mlogCommands) provider.addCompletion(new BasicCompletion(provider, cmd));
        
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(textArea);
        JMenuBar menuBar = new JMenuBar();
        langMenu = new JMenu(lang.getProperty("menu_lang"));
        
        JMenuItem ruItem = new JMenuItem("Русский");
        ruItem.addActionListener(e -> { loadLanguage("ru"); updateUIStrings(); });
        
        JMenuItem enItem = new JMenuItem("English");
        enItem.addActionListener(e -> { loadLanguage("en"); updateUIStrings(); });

        langMenu.add(ruItem);
        langMenu.add(enItem);
        menuBar.add(langMenu);
        setJMenuBar(menuBar);

        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
            theme.apply(textArea);
        } catch (Exception e) { System.err.println("Theme error"); }

        RTextScrollPane sp = new RTextScrollPane(textArea);
        cp.add(sp);

        setContentPane(cp);
        updateUIStrings();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }
    private void loadLanguage(String code) {
        File langFile = new File("lang/" + code + ".lng");
        try (InputStream input = new FileInputStream(langFile)) {
            lang.load(new InputStreamReader(input, "UTF-8"));
        } catch (IOException ex) {
            System.out.println("No .lng file found for " + code + ", using defaults.");
            lang.setProperty("title", "MLG Editor");
            lang.setProperty("menu_lang", "Language");
        }
    }

    private void updateUIStrings() {
        setTitle(lang.getProperty("title"));
        langMenu.setText(lang.getProperty("menu_lang"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}