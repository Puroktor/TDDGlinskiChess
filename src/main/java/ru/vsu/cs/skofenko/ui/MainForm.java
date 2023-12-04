package ru.vsu.cs.skofenko.ui;

import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.IChessPiece;
import ru.vsu.cs.skofenko.logic.chesspiece.ChessPiece;
import ru.vsu.cs.skofenko.logic.service.Coordinate;
import ru.vsu.cs.skofenko.logic.api.board.BoardCell;
import ru.vsu.cs.skofenko.logic.service.GameLogic;
import ru.vsu.cs.skofenko.logic.api.IGameLogic;
import ru.vsu.cs.skofenko.logic.service.GameLogicMapper;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainForm extends JFrame {
    private class BoardPanel extends JPanel {
        private int size = 50;
        private final Polygon[][] board = new Polygon[IGameLogic.N][IGameLogic.N];
        private final Color[] colors = new Color[]{
                new Color(255, 206, 158),
                new Color(232, 171, 111),
                new Color(209, 139, 71)};

        public void setCellSize(int size) {
            this.size = size;
        }

        @Override
        public void paintComponent(Graphics gr) {
            super.paintComponent(gr);
            setRenderHints((Graphics2D) gr);
            if (logic == null) {
                return;
            }
            int half = IGameLogic.N / 2;
            int count = 200;
            BoardCell[][] boardCells = logic.getBoard().getBoard();
            for (int r = -half; r <= half; r++) {
                for (int q = -half; q <= half; q++) {
                    count--;
                    if (Math.abs(r + q) > half)
                        continue;
                    Coordinate cord = Coordinate.createFromAxial(r, q);
                    BoardCell cell = boardCells[cord.getI()][cord.getJ()];
                    if (cell == null) {
                        break;
                    }
                    board[cord.getI()][cord.getJ()] = new Polygon();
                    int centerX = (int) Math.round(size * (1.5 * q + half * 2));
                    int centerY = (int) Math.round(size * (Math.sqrt(3) * (r + 0.5 * q) + half * 2));
                    for (int k = 0; k < 6; k++) {
                        board[cord.getI()][cord.getJ()].addPoint(centerX + (int) Math.round(size * Math.cos(k * 2 * Math.PI / 6)),
                                centerY + (int) Math.round(size * Math.sin(k * 2 * Math.PI / 6)));
                    }
                    switch (cell.getCellType()) {
                        case SELECTED -> paintPolygon(board[cord.getI()][cord.getJ()], gr, Color.YELLOW);
                        case REACHABLE -> {
                            paintPolygon(board[cord.getI()][cord.getJ()], gr, colors[(count) % 3]);
                            drawReachable(gr, centerX, centerY, size / 2);
                        }
                        case CAPTURABLE -> paintPolygon(board[cord.getI()][cord.getJ()], gr, Color.RED);
                        default -> paintPolygon(board[cord.getI()][cord.getJ()], gr, colors[(count) % 3]);
                    }
                    drawChessPiece(gr, cell.getPiece(), centerX, centerY);
                }
            }
        }

        private void setRenderHints(Graphics2D gr) {
            gr.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gr.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        }

        private void drawChessPiece(Graphics gr, IChessPiece piece, int x, int y) {
            if (piece != null) {
                gr.drawImage(ChessPieceIcons.getIcon(piece), x - size / 2, y - size / 2, size, size, null);
            }
        }

        private void paintPolygon(Polygon p, Graphics gr, Color color) {
            gr.setColor(color);
            gr.fillPolygon(p);
            gr.setColor(Color.BLACK);
            gr.drawPolygon(p);
        }

        private void drawReachable(Graphics gr, int centerX, int centerY, int size) {
            gr.setColor(Color.YELLOW);
            gr.fillOval(centerX - size / 2, centerY - size / 2, size, size);
        }

        public void panelClicked(int x, int y) {
            for (int i = 0; i < IGameLogic.N; i++) {
                for (int j = 0; j < IGameLogic.N; j++) {
                    if (board[i][j] != null && board[i][j].contains(x, y)) {
                        if (logic.clickCell(Coordinate.createFromInner(i, j))) {
                            logic.promotePawn(showDialogWindow());
                        }
                        return;
                    }
                }
            }
        }
    }

    private class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            BoardPanel panel = (BoardPanel) e.getComponent();
            panel.setCellSize((int) Math.round(Math.min(panel.getWidth(), panel.getHeight()) / (1.8 * IGameLogic.N)));
        }
    }

    private JPanel mainPanel;
    private JPanel boardPanelCont;
    private JPanel player2Panel;
    private JPanel player1Panel;
    private JLabel stateLabel;
    private JLabel player2Label;
    private JLabel player1Label;
    private FileFilter fileFilter;

    private final ResourceBundle bundle;
    private IGameLogic logic;

    public MainForm(Locale locale) {
        bundle = ResourceBundle.getBundle("main-form", locale);
        this.setTitle(bundle.getString("app.title"));
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();

        player1Label.setText(bundle.getString("player1.name"));
        player2Label.setText(bundle.getString("player2.name"));

        setSize(640, 480);
        setExtendedState(MAXIMIZED_BOTH);

        logic = new GameLogic();
        stateLabel.setText(bundle.getString(logic.getGameState().name().toLowerCase()));

        boardPanelCont.setLayout(new GridBagLayout());
        SquarePanel square = new SquarePanel();
        boardPanelCont.add(square);
        square.setLayout(new BorderLayout());
        square.setVisible(true);

        BoardPanel boardPanel = new BoardPanel();
        square.add(boardPanel, BorderLayout.CENTER);
        boardPanel.addComponentListener(new ResizeListener());

        initializeMenu();
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ((BoardPanel) e.getComponent()).panelClicked(e.getX(), e.getY());
                repaint();
            }
        });
    }

    private void initializeMenu() {
        fileFilter = new FileNameExtensionFilter(bundle.getString("save.files"),
                GameLogicMapper.FILE_EXTENSION.substring(1));
        JMenuItem saveMenu = new JMenuItem(bundle.getString("save.game"));
        saveMenu.setToolTipText(bundle.getString("save.game.tip"));
        saveMenu.addActionListener(this::saveCurrentGame);
        JMenuItem openMenu = new JMenuItem(bundle.getString("open.game"));
        openMenu.setToolTipText(bundle.getString("open.game.tip"));
        openMenu.addActionListener(this::openSavedGame);

        JMenu gameMenu = new JMenu(bundle.getString("game"));
        gameMenu.add(saveMenu);
        gameMenu.add(openMenu);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createEmptyBorder());
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    public void saveCurrentGame(ActionEvent e) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(fileFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(GameLogicMapper.FILE_EXTENSION)) {
                    selectedFile = new File(selectedFile + GameLogicMapper.FILE_EXTENSION);
                }
                GameLogicMapper.saveGame(logic, selectedFile);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    bundle.getString("error.save"),
                    bundle.getString("error.title"),
                    JOptionPane.PLAIN_MESSAGE,
                    null);
        }
    }

    public void openSavedGame(ActionEvent e) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(fileFilter);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setDialogTitle(bundle.getString("choose.file"));
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                logic = GameLogicMapper.openGame(selectedFile);
                this.repaint();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    bundle.getString("error.open"),
                    bundle.getString("error.title"),
                    JOptionPane.PLAIN_MESSAGE,
                    null);
        }
    }

    public ChessPiece showDialogWindow() {
        String[] possibilities = bundle.getString("option.possibilities").split("[ \\t]+");
        ResourceBundle engBundle = ResourceBundle.getBundle("main-form", new ResourceBundle.Control() {
            @Override
            public List<Locale> getCandidateLocales(String name, Locale locale) {
                return Collections.singletonList(Locale.ROOT);
            }
        });
        String[] engPossibilities = engBundle.getString("option.possibilities").split("[ \\t]+");
        while (true) {
            try {
                String str = (String) JOptionPane.showInputDialog(
                        this,
                        bundle.getString("option.mes"),
                        bundle.getString("option.title"),
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        possibilities,
                        possibilities[0]);

                return ChessPiece.getChessPieceFromStr(
                        engPossibilities[Arrays.asList(possibilities).indexOf(str)], logic.getNowTurn());
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException ignored) {
            }
        }
    }

    @Override
    public void repaint() {
        if (logic.getNowTurn() == ChessColor.WHITE) {
            player1Panel.setBackground(Color.RED);
            player2Panel.setBackground(Color.LIGHT_GRAY);
        } else {
            player2Panel.setBackground(Color.RED);
            player1Panel.setBackground(Color.LIGHT_GRAY);
        }
        stateLabel.setText(bundle.getString(logic.getGameState().name().toLowerCase()));
        super.repaint();
    }
}
