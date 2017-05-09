package ui;

import org.apache.commons.io.IOUtils;
import org.inspector.ResultAttributes;
import org.inspector.SourceStructure;
import org.inspector.java.JavaInspector;
import org.inspector.java.JavaParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

@SuppressWarnings("unused")
public class UserInterface extends JFrame {
    private JTextArea textArea2;
    private JButton compareButton;
    private JButton selectButton1;
    private JButton selectButton;
    private JTextArea textArea1;
    private JPanel UIRoot;
    private JLabel AppLabel;
    private JScrollPane scrollAreaPane1;
    private JScrollPane scrollAreaPane2;
    private JPanel resultsPanel;
    private JPanel resultsNamesBox;
    private JPanel resultsValuesBox;
    private JLabel resultsLabel;

    /**
     * Results Labels
     */
    private JLabel Classes;
    private JLabel Procedures;
    private JLabel LocalVariables;
    private JLabel Comments;
    private JLabel Dependencies;

    private JLabel ClassesValue;
    private JLabel ProceduresValue;
    private JLabel IdentifiersValue;
    private JLabel DependenciesValue;
    private JLabel imageLabel;
    private JTextArea textArea3;

    private final JLabel resultsLabels[] = {Classes, Procedures, LocalVariables, Comments, Dependencies };
    private final JLabel resultsValuesLabels[] =
            {ClassesValue, ProceduresValue, IdentifiersValue, DependenciesValue};

    private class Source
    {
        String content;

        Source(String text)
        {
            content = text;
        }
    }

    Source leftHandSource = new Source("");
    Source rightHandSource = new Source("");

    private String getFileText(File file)
    {
        String result = "";

        String path = file.getPath();

        if(path.endsWith(".java") || path.endsWith(".cpp") || path.endsWith(".hpp") || path.endsWith(".py")) {

            try {
                FileInputStream in = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                int res = in.read(data);
                in.close();

                result = new String(data);
            } catch (final Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    private DropTarget setupDropTarget(JTextArea textArea, Source capture)
    {
        return new DropTarget(){

            @SuppressWarnings("unchecked")
            public synchronized void drop(DropTargetDropEvent event)
            {
                try
                {
                    event.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> files = (List<File>)event.getTransferable().getTransferData(
                            DataFlavor.javaFileListFlavor);

                    String text = getFileText(files.get(0));
                    textArea.setText(text);
                    capture.content = text;
                }
                catch(final Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
    }

    private void setupSelectButton(JButton selectionButton, JTextArea textArea)
    {
        selectionButton.addActionListener( (ActionEvent e) ->
            {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(UIRoot);

                if(result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        textArea.setText(getFileText(file));
                    } catch (final Exception error) {
                        error.printStackTrace();
                    }
                }
            });
    }

    private void setupDummyActionCompareButton(JButton compareButton)
    {
        compareButton.addActionListener( (ActionEvent e) ->
        {
            JavaParser parser = new JavaParser();

            try {
                SourceStructure struct1 =
                        parser.parseSource(IOUtils.toInputStream(leftHandSource.content, "UTF-8"));
                SourceStructure struct2 =
                        parser.parseSource(IOUtils.toInputStream(rightHandSource.content, "UTF-8"));

                JavaInspector inspector = new JavaInspector();

                ResultAttributes attribs = inspector.inspectSource(struct1, struct2);

                ClassesValue.setText(Integer.toString(attribs.numSharedClasses));
                ProceduresValue.setText(Integer.toString(attribs.numSharedProcedures));
                DependenciesValue.setText(Integer.toString(attribs.numSharedDependencies));
                IdentifiersValue.setText(Integer.toString(attribs.numSharedLocalVariables));
                StringBuilder sb = new StringBuilder();
                for(String info : attribs.sourceInfo)
                {
                    sb.append(info);
                }
                textArea3.setText(sb.toString());

            } catch(final Exception excep)
            {
                excep.printStackTrace();
            }
        });
    }

    public UserInterface()
    {
        super("Inspector");

        ImageIcon logoImage = new ImageIcon("logo.png");
        this.setIconImage(logoImage.getImage());

        textArea1.setDropTarget(setupDropTarget(textArea1, leftHandSource));
        textArea2.setDropTarget(setupDropTarget(textArea2, rightHandSource));

        setupSelectButton(selectButton, textArea1);
        setupSelectButton(selectButton1, textArea2);

        setupDummyActionCompareButton(compareButton);

        try {
            BufferedImage logoImageDisplay = ImageIO.read(new File("logo.png"));
            ImageIcon icon = new ImageIcon(logoImageDisplay);
            imageLabel.setIcon(icon);
        }
        catch(final Exception e)
        {
            e.printStackTrace();
        }

        setContentPane(UIRoot);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);

        setMinimumSize(new Dimension(1200,800));
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
