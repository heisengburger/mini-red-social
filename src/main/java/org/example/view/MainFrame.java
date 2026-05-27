package org.example.view;

import org.example.controller.InteresController;
import org.example.controller.PublicacionController;
import org.example.controller.RedSocialController;
import org.example.controller.UsuarioController;
import org.example.model.Interes;
import org.example.model.Publicacion;
import org.example.model.Usuario;
import org.example.repository.InteresRepository;
import org.example.repository.PublicacionRepository;
import org.example.repository.RedSocialRepository;
import org.example.repository.UsuarioRepository;
import org.neo4j.driver.Driver;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private static final Color APP_BACKGROUND = new Color(245, 247, 250);
    private static final Color SURFACE = Color.WHITE;
    private static final Color SURFACE_ALT = new Color(232, 245, 243);
    private static final Color BORDER = new Color(221, 226, 232);
    private static final Color TEXT = new Color(31, 41, 55);
    private static final Color MUTED = new Color(107, 114, 128);
    private static final Color ACCENT = new Color(15, 118, 110);
    private static final Color ACCENT_DARK = new Color(17, 94, 89);
    private static final Color WARNING = new Color(180, 83, 9);
    private static final Font TITLE_FONT = new Font("Dialog", Font.BOLD, 24);
    private static final Font SECTION_FONT = new Font("Dialog", Font.BOLD, 16);
    private static final Font BODY_FONT = new Font("Dialog", Font.PLAIN, 13);

    private final UsuarioController usuarioController;
    private final PublicacionController publicacionController;
    private final InteresController interesController;
    private final RedSocialController redSocialController;
    private final Runnable onClose;
    private final CardLayout contentLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(contentLayout);
    private final JLabel pageTitleLabel = new JLabel();
    private final JLabel pageSubtitleLabel = new JLabel();
    private final JLabel statusLabel = new JLabel("Conectado a Neo4j");

    private JTable usuariosTable;
    private JTable publicacionesTable;
    private JTable interesesTable;
    private JTable consultasTable;
    private JPanel dashboardMetricsPanel;

    public MainFrame(Driver driver, Runnable onClose) {
        this.usuarioController = new UsuarioController(new UsuarioRepository(driver));
        this.publicacionController = new PublicacionController(new PublicacionRepository(driver));
        this.interesController = new InteresController(new InteresRepository(driver));
        this.redSocialController = new RedSocialController(new RedSocialRepository(driver));
        this.onClose = onClose;

        configureFrame();
        buildLayout();
        cargarTablas();
        showPage("dashboard", "Panel general", "Resumen de datos y actividad principal");
    }

    private void configureFrame() {
        setTitle("Mini Red Social - Neo4j");
        setMinimumSize(new Dimension(1180, 760));
        setSize(1240, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                cerrar();
            }
        });
    }

    private void buildLayout() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(APP_BACKGROUND);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildMainArea(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(26, 32, 44));
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 18, 18, 18));

        JLabel brand = new JLabel("Mini Red Social");
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Dialog", Font.BOLD, 22));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel stack = new JLabel("Java Swing + Neo4j");
        stack.setForeground(new Color(203, 213, 225));
        stack.setFont(BODY_FONT);
        stack.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(brand);
        sidebar.add(Box.createVerticalStrut(4));
        sidebar.add(stack);
        sidebar.add(Box.createVerticalStrut(28));
        sidebar.add(navButton("Panel general", "dashboard", "Panel general", "Resumen de datos y actividad principal"));
        sidebar.add(navButton("Usuarios", "usuarios", "Usuarios", "Gestion de perfiles de la red"));
        sidebar.add(navButton("Publicaciones", "publicaciones", "Publicaciones", "Contenido creado por usuarios"));
        sidebar.add(navButton("Intereses", "intereses", "Intereses", "Temas compartidos por la comunidad"));
        sidebar.add(navButton("Relaciones", "relaciones", "Relaciones", "Amistades, intereses y likes"));
        sidebar.add(navButton("Consultas", "consultas", "Consultas", "Recomendaciones y analisis del grafo"));
        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("ABP Bases NoSQL");
        footer.setForeground(new Color(148, 163, 184));
        footer.setFont(new Font("Dialog", Font.PLAIN, 12));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(footer);
        return sidebar;
    }

    private JButton navButton(String text, String page, String title, String subtitle) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        button.setBackground(new Color(45, 55, 72));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Dialog", Font.BOLD, 13));
        button.addActionListener(event -> showPage(page, title, subtitle));
        return button;
    }

    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(APP_BACKGROUND);
        main.add(buildHeader(), BorderLayout.NORTH);

        contentPanel.setBackground(APP_BACKGROUND);
        contentPanel.add(buildDashboardPanel(), "dashboard");
        contentPanel.add(buildUsuariosPanel(), "usuarios");
        contentPanel.add(buildPublicacionesPanel(), "publicaciones");
        contentPanel.add(buildInteresesPanel(), "intereses");
        contentPanel.add(buildRelacionesPanel(), "relaciones");
        contentPanel.add(buildConsultasPanel(), "consultas");
        main.add(contentPanel, BorderLayout.CENTER);
        return main;
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(APP_BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(22, 28, 12, 28));

        pageTitleLabel.setFont(TITLE_FONT);
        pageTitleLabel.setForeground(TEXT);
        pageSubtitleLabel.setFont(BODY_FONT);
        pageSubtitleLabel.setForeground(MUTED);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(pageTitleLabel);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(pageSubtitleLabel);

        statusLabel.setFont(new Font("Dialog", Font.BOLD, 12));
        statusLabel.setForeground(ACCENT_DARK);
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(153, 220, 211)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        header.add(titlePanel, BorderLayout.WEST);
        header.add(statusLabel, BorderLayout.EAST);
        return header;
    }

    private JPanel buildDashboardPanel() {
        JPanel root = pagePanel();
        dashboardMetricsPanel = new JPanel(new GridLayout(1, 4, 12, 12));
        dashboardMetricsPanel.setOpaque(false);
        root.add(dashboardMetricsPanel, BorderLayout.NORTH);

        JPanel lower = new JPanel(new GridLayout(1, 2, 12, 12));
        lower.setOpaque(false);
        lower.add(surfaceWithTitle("Top publicaciones", createDashboardLikesTable()));
        lower.add(surfaceWithTitle("Sugerencias para Ana", createDashboardSuggestionsTable()));
        root.add(lower, BorderLayout.CENTER);
        return root;
    }

    private JScrollPane createDashboardLikesTable() {
        JTable table = createTable("ID", "Texto", "Autor", "Likes");
        DefaultTableModel model = model("ID", "Texto", "Autor", "Likes");
        for (RedSocialRepository.PublicacionPopular publicacion : redSocialController.listarPublicacionesConLikes()) {
            model.addRow(new Object[]{
                    publicacion.idPublicacion(),
                    publicacion.texto(),
                    publicacion.autor(),
                    publicacion.numeroLikes()
            });
        }
        table.setModel(model);
        return new JScrollPane(table);
    }

    private JScrollPane createDashboardSuggestionsTable() {
        JTable table = createTable("Sugerencia", "Comun", "Conectado por");
        DefaultTableModel model = model("Sugerencia", "Comun", "Conectado por");
        for (RedSocialRepository.SugerenciaAmistad sugerencia : redSocialController.sugerirAmigos("Ana")) {
            model.addRow(new Object[]{
                    sugerencia.sugerencia(),
                    sugerencia.amigosEnComun(),
                    sugerencia.conectadoPor()
            });
        }
        table.setModel(model);
        return new JScrollPane(table);
    }

    private JPanel buildUsuariosPanel() {
        usuariosTable = createTable("ID", "Nombre", "Edad", "Ciudad");

        JTextField idField = field();
        JTextField nombreField = field();
        JTextField edadField = field();
        JTextField ciudadField = field();

        usuariosTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && usuariosTable.getSelectedRow() >= 0) {
                int row = usuariosTable.convertRowIndexToModel(usuariosTable.getSelectedRow());
                idField.setText(valueAt(usuariosTable, row, 0));
                nombreField.setText(valueAt(usuariosTable, row, 1));
                edadField.setText(valueAt(usuariosTable, row, 2));
                ciudadField.setText(valueAt(usuariosTable, row, 3));
            }
        });

        JPanel form = formPanel();
        addField(form, "ID", idField, 0);
        addField(form, "Nombre", nombreField, 1);
        addField(form, "Edad", edadField, 2);
        addField(form, "Ciudad", ciudadField, 3);

        JButton crearButton = primaryButton("Crear");
        crearButton.addActionListener(event -> runAction("Usuario creado", () -> {
            usuarioController.crearUsuario(
                    parseInt(idField, "ID"),
                    required(nombreField, "Nombre"),
                    parseInt(edadField, "Edad"),
                    required(ciudadField, "Ciudad")
            );
            cargarUsuarios();
            refrescarDashboard();
        }));

        JButton actualizarButton = secondaryButton("Actualizar");
        actualizarButton.addActionListener(event -> runAction("Usuario actualizado", () -> {
            usuarioController.actualizarUsuario(
                    parseInt(idField, "ID"),
                    required(nombreField, "Nombre"),
                    parseInt(edadField, "Edad"),
                    required(ciudadField, "Ciudad")
            );
            cargarUsuarios();
        }));

        JButton eliminarButton = dangerButton("Eliminar");
        eliminarButton.addActionListener(event -> runAction("Usuario eliminado", () -> {
            usuarioController.eliminarUsuario(parseInt(idField, "ID"));
            cargarTablas();
            refrescarDashboard();
        }));

        JButton recargarButton = secondaryButton("Recargar");
        recargarButton.addActionListener(event -> cargarUsuarios());

        return dataPage(form, buttonPanel(crearButton, actualizarButton, eliminarButton, recargarButton), usuariosTable);
    }

    private JPanel buildPublicacionesPanel() {
        publicacionesTable = createTable("ID", "Texto", "Fecha");

        JTextField autorIdField = field();
        JTextField idField = field();
        JTextArea textoArea = area();
        JTextField fechaField = field();

        publicacionesTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && publicacionesTable.getSelectedRow() >= 0) {
                int row = publicacionesTable.convertRowIndexToModel(publicacionesTable.getSelectedRow());
                idField.setText(valueAt(publicacionesTable, row, 0));
                textoArea.setText(valueAt(publicacionesTable, row, 1));
                fechaField.setText(valueAt(publicacionesTable, row, 2));
            }
        });

        JPanel form = formPanel();
        addField(form, "Autor ID", autorIdField, 0);
        addField(form, "Publicacion ID", idField, 1);
        addArea(form, "Texto", textoArea, 2);
        addField(form, "Fecha", fechaField, 3);

        JButton crearButton = primaryButton("Crear");
        crearButton.addActionListener(event -> runAction("Publicacion creada", () -> {
            redSocialController.crearPublicacion(
                    parseInt(autorIdField, "Autor ID"),
                    parseInt(idField, "Publicacion ID"),
                    required(textoArea, "Texto"),
                    required(fechaField, "Fecha")
            );
            cargarPublicaciones();
            refrescarDashboard();
        }));

        JButton actualizarButton = secondaryButton("Actualizar");
        actualizarButton.addActionListener(event -> runAction("Publicacion actualizada", () -> {
            publicacionController.actualizarPublicacion(
                    parseInt(idField, "Publicacion ID"),
                    required(textoArea, "Texto"),
                    required(fechaField, "Fecha")
            );
            cargarPublicaciones();
            refrescarDashboard();
        }));

        JButton eliminarButton = dangerButton("Eliminar");
        eliminarButton.addActionListener(event -> runAction("Publicacion eliminada", () -> {
            publicacionController.eliminarPublicacion(parseInt(idField, "Publicacion ID"));
            cargarPublicaciones();
            refrescarDashboard();
        }));

        JButton recargarButton = secondaryButton("Recargar");
        recargarButton.addActionListener(event -> cargarPublicaciones());

        return dataPage(form, buttonPanel(crearButton, actualizarButton, eliminarButton, recargarButton), publicacionesTable);
    }

    private JPanel buildInteresesPanel() {
        interesesTable = createTable("Nombre");

        JTextField nombreField = field();
        JTextField nuevoNombreField = field();

        interesesTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && interesesTable.getSelectedRow() >= 0) {
                int row = interesesTable.convertRowIndexToModel(interesesTable.getSelectedRow());
                nombreField.setText(valueAt(interesesTable, row, 0));
            }
        });

        JPanel form = formPanel();
        addField(form, "Nombre", nombreField, 0);
        addField(form, "Nuevo nombre", nuevoNombreField, 1);

        JButton crearButton = primaryButton("Crear");
        crearButton.addActionListener(event -> runAction("Interes creado", () -> {
            interesController.crearInteres(required(nombreField, "Nombre"));
            cargarIntereses();
            refrescarDashboard();
        }));

        JButton renombrarButton = secondaryButton("Renombrar");
        renombrarButton.addActionListener(event -> runAction("Interes actualizado", () -> {
            interesController.actualizarInteres(
                    required(nombreField, "Nombre"),
                    required(nuevoNombreField, "Nuevo nombre")
            );
            cargarIntereses();
        }));

        JButton eliminarButton = dangerButton("Eliminar");
        eliminarButton.addActionListener(event -> runAction("Interes eliminado", () -> {
            interesController.eliminarInteres(required(nombreField, "Nombre"));
            cargarIntereses();
            refrescarDashboard();
        }));

        JButton recargarButton = secondaryButton("Recargar");
        recargarButton.addActionListener(event -> cargarIntereses());

        return dataPage(form, buttonPanel(crearButton, renombrarButton, eliminarButton, recargarButton), interesesTable);
    }

    private JPanel buildRelacionesPanel() {
        JPanel root = pagePanel();
        JPanel grid = new JPanel(new GridLayout(1, 3, 12, 12));
        grid.setOpaque(false);

        JTextField usuarioIdField = field("1");
        JTextField amigoIdField = field("7");
        JTextField desdeField = field("2025");
        JPanel amistadForm = formPanel();
        addField(amistadForm, "Usuario ID", usuarioIdField, 0);
        addField(amistadForm, "Amigo ID", amigoIdField, 1);
        addField(amistadForm, "Desde", desdeField, 2);
        JButton crearAmistadButton = primaryButton("Crear amistad");
        crearAmistadButton.addActionListener(event -> runAction("Amistad creada", () -> {
            redSocialController.crearAmistad(parseInt(usuarioIdField, "Usuario ID"), parseInt(amigoIdField, "Amigo ID"),
                    required(desdeField, "Desde"));
            refrescarDashboard();
        }));
        JButton eliminarAmistadButton = dangerButton("Eliminar amistad");
        eliminarAmistadButton.addActionListener(event -> runAction("Amistad eliminada", () -> {
            redSocialController.eliminarAmistad(parseInt(usuarioIdField, "Usuario ID"), parseInt(amigoIdField, "Amigo ID"));
            refrescarDashboard();
        }));
        grid.add(surfaceWithTitle("Amistades", stack(amistadForm, buttonPanel(crearAmistadButton, eliminarAmistadButton))));

        JTextField interesUsuarioIdField = field("1");
        JTextField interesNombreField = field("Programacion");
        JPanel interesForm = formPanel();
        addField(interesForm, "Usuario ID", interesUsuarioIdField, 0);
        addField(interesForm, "Interes", interesNombreField, 1);
        JButton asignarInteresButton = primaryButton("Asignar interes");
        asignarInteresButton.addActionListener(event -> runAction("Interes asignado", () -> {
            redSocialController.asignarInteres(parseInt(interesUsuarioIdField, "Usuario ID"),
                    required(interesNombreField, "Interes"));
            cargarIntereses();
            refrescarDashboard();
        }));
        grid.add(surfaceWithTitle("Intereses", stack(interesForm, buttonPanel(asignarInteresButton))));

        JTextField likeUsuarioIdField = field("1");
        JTextField likePublicacionIdField = field("3");
        JPanel likeForm = formPanel();
        addField(likeForm, "Usuario ID", likeUsuarioIdField, 0);
        addField(likeForm, "Publicacion ID", likePublicacionIdField, 1);
        JButton likeButton = primaryButton("Dar like");
        likeButton.addActionListener(event -> runAction("Like registrado", () -> {
            redSocialController.darLike(parseInt(likeUsuarioIdField, "Usuario ID"),
                    parseInt(likePublicacionIdField, "Publicacion ID"));
            refrescarDashboard();
        }));
        grid.add(surfaceWithTitle("Likes", stack(likeForm, buttonPanel(likeButton))));

        root.add(grid, BorderLayout.NORTH);
        root.add(surfaceWithTitle("Datos activos", buildOverviewTables()), BorderLayout.CENTER);
        return root;
    }

    private JPanel buildOverviewTables() {
        JPanel tables = new JPanel(new GridLayout(1, 3, 10, 10));
        tables.setOpaque(false);
        tables.add(new JScrollPane(snapshotUsuariosTable()));
        tables.add(new JScrollPane(snapshotPublicacionesTable()));
        tables.add(new JScrollPane(snapshotInteresesTable()));
        return tables;
    }

    private JTable snapshotUsuariosTable() {
        JTable table = createTable("ID", "Nombre", "Ciudad");
        DefaultTableModel model = model("ID", "Nombre", "Ciudad");
        for (Usuario usuario : usuarioController.listarUsuarios()) {
            model.addRow(new Object[]{usuario.getId(), usuario.getNombre(), usuario.getCiudad()});
        }
        table.setModel(model);
        return table;
    }

    private JTable snapshotPublicacionesTable() {
        JTable table = createTable("ID", "Texto", "Fecha");
        DefaultTableModel model = model("ID", "Texto", "Fecha");
        for (Publicacion publicacion : publicacionController.listarPublicaciones()) {
            model.addRow(new Object[]{publicacion.getId(), publicacion.getTexto(), publicacion.getFecha()});
        }
        table.setModel(model);
        return table;
    }

    private JTable snapshotInteresesTable() {
        JTable table = createTable("Nombre");
        DefaultTableModel model = model("Nombre");
        for (Interes interes : interesController.listarIntereses()) {
            model.addRow(new Object[]{interes.getNombre()});
        }
        table.setModel(model);
        return table;
    }

    private JPanel buildConsultasPanel() {
        consultasTable = createTable("Resultado", "Dato 1", "Dato 2", "Dato 3", "Dato 4");

        JTextField usuarioField = field("Ana");
        JTextField origenField = field("Ana");
        JTextField destinoField = field("Sofia");

        JPanel form = formPanel();
        addField(form, "Usuario", usuarioField, 0);
        addField(form, "Origen", origenField, 1);
        addField(form, "Destino", destinoField, 2);

        JButton sugerenciasButton = primaryButton("Amigos sugeridos");
        sugerenciasButton.addActionListener(event -> runAction("Consulta ejecutada",
                () -> mostrarSugerencias(required(usuarioField, "Usuario"))));

        JButton interesesButton = secondaryButton("Intereses comunes");
        interesesButton.addActionListener(event -> runAction("Consulta ejecutada",
                () -> mostrarInteresesComunes(required(usuarioField, "Usuario"))));

        JButton likesButton = secondaryButton("Mas likes");
        likesButton.addActionListener(event -> runAction("Consulta ejecutada", this::mostrarPublicacionesConLikes));

        JButton caminoButton = secondaryButton("Camino corto");
        caminoButton.addActionListener(event -> runAction("Consulta ejecutada",
                () -> mostrarCamino(required(origenField, "Origen"), required(destinoField, "Destino"))));

        JButton ciudadesButton = secondaryButton("Ciudades");
        ciudadesButton.addActionListener(event -> runAction("Consulta ejecutada", this::mostrarComunidades));

        return dataPage(form, buttonPanel(sugerenciasButton, interesesButton, likesButton, caminoButton, ciudadesButton),
                consultasTable);
    }

    private void showPage(String page, String title, String subtitle) {
        pageTitleLabel.setText(title);
        pageSubtitleLabel.setText(subtitle);
        contentLayout.show(contentPanel, page);
        if ("dashboard".equals(page)) {
            refrescarDashboard();
        }
    }

    private void cargarTablas() {
        cargarUsuarios();
        cargarPublicaciones();
        cargarIntereses();
    }

    private void cargarUsuarios() {
        List<Usuario> usuarios = usuarioController.listarUsuarios();
        DefaultTableModel model = model("ID", "Nombre", "Edad", "Ciudad");
        for (Usuario usuario : usuarios) {
            model.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getEdad(),
                    usuario.getCiudad()
            });
        }
        usuariosTable.setModel(model);
        ajustarColumnas(usuariosTable);
    }

    private void cargarPublicaciones() {
        List<Publicacion> publicaciones = publicacionController.listarPublicaciones();
        DefaultTableModel model = model("ID", "Texto", "Fecha");
        for (Publicacion publicacion : publicaciones) {
            model.addRow(new Object[]{
                    publicacion.getId(),
                    publicacion.getTexto(),
                    publicacion.getFecha()
            });
        }
        publicacionesTable.setModel(model);
        ajustarColumnas(publicacionesTable);
    }

    private void cargarIntereses() {
        List<Interes> intereses = interesController.listarIntereses();
        DefaultTableModel model = model("Nombre");
        for (Interes interes : intereses) {
            model.addRow(new Object[]{interes.getNombre()});
        }
        interesesTable.setModel(model);
        ajustarColumnas(interesesTable);
    }

    private void refrescarDashboard() {
        if (dashboardMetricsPanel == null) {
            return;
        }

        int usuarios = usuarioController.listarUsuarios().size();
        int publicaciones = publicacionController.listarPublicaciones().size();
        int intereses = interesController.listarIntereses().size();
        int sugerenciasAna = redSocialController.sugerirAmigos("Ana").size();

        dashboardMetricsPanel.removeAll();
        dashboardMetricsPanel.add(metric("Usuarios", String.valueOf(usuarios), ACCENT));
        dashboardMetricsPanel.add(metric("Publicaciones", String.valueOf(publicaciones), new Color(79, 70, 229)));
        dashboardMetricsPanel.add(metric("Intereses", String.valueOf(intereses), WARNING));
        dashboardMetricsPanel.add(metric("Sugerencias Ana", String.valueOf(sugerenciasAna), new Color(5, 150, 105)));
        dashboardMetricsPanel.revalidate();
        dashboardMetricsPanel.repaint();
    }

    private JPanel metric(String label, String value, Color color) {
        JPanel panel = surface();
        panel.setLayout(new BorderLayout());
        JLabel labelView = new JLabel(label);
        labelView.setForeground(MUTED);
        labelView.setFont(new Font("Dialog", Font.BOLD, 12));
        JLabel valueView = new JLabel(value);
        valueView.setForeground(color);
        valueView.setFont(new Font("Dialog", Font.BOLD, 34));
        panel.add(labelView, BorderLayout.NORTH);
        panel.add(valueView, BorderLayout.CENTER);
        return panel;
    }

    private void mostrarSugerencias(String usuario) {
        DefaultTableModel model = model("Sugerencia", "Amigos en comun", "Conectado por");
        for (RedSocialRepository.SugerenciaAmistad sugerencia : redSocialController.sugerirAmigos(usuario)) {
            model.addRow(new Object[]{
                    sugerencia.sugerencia(),
                    sugerencia.amigosEnComun(),
                    sugerencia.conectadoPor()
            });
        }
        consultasTable.setModel(model);
        ajustarColumnas(consultasTable);
    }

    private void mostrarInteresesComunes(String usuario) {
        DefaultTableModel model = model("Usuario", "Intereses comunes", "Total");
        for (RedSocialRepository.InteresComun interesComun : redSocialController.buscarInteresesComunes(usuario)) {
            model.addRow(new Object[]{
                    interesComun.usuario(),
                    interesComun.interesesComunes(),
                    interesComun.totalInteresesComunes()
            });
        }
        consultasTable.setModel(model);
        ajustarColumnas(consultasTable);
    }

    private void mostrarPublicacionesConLikes() {
        DefaultTableModel model = model("ID", "Texto", "Autor", "Likes", "Usuarios");
        for (RedSocialRepository.PublicacionPopular publicacion : redSocialController.listarPublicacionesConLikes()) {
            model.addRow(new Object[]{
                    publicacion.idPublicacion(),
                    publicacion.texto(),
                    publicacion.autor(),
                    publicacion.numeroLikes(),
                    publicacion.usuariosQueDieronLike()
            });
        }
        consultasTable.setModel(model);
        ajustarColumnas(consultasTable);
    }

    private void mostrarCamino(String origen, String destino) {
        RedSocialRepository.CaminoUsuario camino = redSocialController.buscarCaminoMasCorto(origen, destino);
        DefaultTableModel model = model("Origen", "Destino", "Camino", "Saltos");
        model.addRow(new Object[]{origen, destino, camino.caminoMasCorto(), camino.numeroDeSaltos()});
        consultasTable.setModel(model);
        ajustarColumnas(consultasTable);
    }

    private void mostrarComunidades() {
        DefaultTableModel model = model("Ciudad", "Total usuarios", "Usuarios");
        for (RedSocialRepository.ComunidadCiudad comunidad : redSocialController.listarComunidadesPorCiudad()) {
            model.addRow(new Object[]{
                    comunidad.ciudad(),
                    comunidad.totalUsuarios(),
                    comunidad.usuarios()
            });
        }
        consultasTable.setModel(model);
        ajustarColumnas(consultasTable);
    }

    private JPanel dataPage(JPanel form, JPanel buttons, JTable table) {
        JPanel root = pagePanel();
        JPanel left = surfaceWithTitle("Acciones", stack(form, buttons));
        left.setPreferredSize(new Dimension(330, 0));
        root.add(left, BorderLayout.WEST);
        root.add(surfaceWithTitle("Registros", new JScrollPane(table)), BorderLayout.CENTER);
        return root;
    }

    private JPanel pagePanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(APP_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 28, 28, 28));
        return panel;
    }

    private JPanel surfaceWithTitle(String title, Component content) {
        JPanel panel = surface();
        panel.setLayout(new BorderLayout(10, 10));
        JLabel label = new JLabel(title);
        label.setFont(SECTION_FONT);
        label.setForeground(TEXT);
        panel.add(label, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private JPanel surface() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return panel;
    }

    private JPanel stack(Component first, Component second) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.add(first);
        panel.add(Box.createVerticalStrut(12));
        panel.add(second);
        return panel;
    }

    private JPanel formPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        return panel;
    }

    private void addField(JPanel panel, String label, JTextField field, int row) {
        int baseRow = row * 2;
        JLabel labelView = new JLabel(label);
        labelView.setForeground(TEXT);
        labelView.setFont(new Font("Dialog", Font.BOLD, 12));
        GridBagConstraints labelConstraints = constraints(0, baseRow);
        labelConstraints.anchor = GridBagConstraints.WEST;
        panel.add(labelView, labelConstraints);

        GridBagConstraints fieldConstraints = constraints(0, baseRow + 1);
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, fieldConstraints);
    }

    private void addArea(JPanel panel, String label, JTextArea area, int row) {
        int baseRow = row * 2;
        JLabel labelView = new JLabel(label);
        labelView.setForeground(TEXT);
        labelView.setFont(new Font("Dialog", Font.BOLD, 12));
        GridBagConstraints labelConstraints = constraints(0, baseRow);
        labelConstraints.anchor = GridBagConstraints.WEST;
        panel.add(labelView, labelConstraints);

        GridBagConstraints areaConstraints = constraints(0, baseRow + 1);
        areaConstraints.weightx = 1;
        areaConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JScrollPane(area), areaConstraints);
    }

    private GridBagConstraints constraints(int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(4, 0, 4, 0);
        return constraints;
    }

    private JPanel buttonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panel.setOpaque(false);
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }

    private JTextField field() {
        return field("");
    }

    private JTextField field(String text) {
        JTextField field = new JTextField(text, 18);
        field.setFont(BODY_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JTextArea area() {
        JTextArea area = new JTextArea(4, 18);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(BODY_FONT);
        area.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return area;
    }

    private JButton primaryButton(String text) {
        return styledButton(text, ACCENT, Color.WHITE);
    }

    private JButton secondaryButton(String text) {
        return styledButton(text, SURFACE_ALT, ACCENT_DARK);
    }

    private JButton dangerButton(String text) {
        return styledButton(text, new Color(254, 242, 242), new Color(153, 27, 27));
    }

    private JButton styledButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(new Font("Dialog", Font.BOLD, 12));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background.equals(ACCENT) ? ACCENT_DARK : BORDER),
                BorderFactory.createEmptyBorder(9, 12, 9, 12)
        ));
        return button;
    }

    private JTable createTable(String... columns) {
        JTable table = new JTable(model(columns));
        table.setRowHeight(32);
        table.setFont(BODY_FONT);
        table.setForeground(TEXT);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(204, 251, 241));
        table.setSelectionForeground(TEXT);
        table.setGridColor(new Color(238, 242, 247));
        table.setShowVerticalLines(false);
        table.setAutoCreateRowSorter(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Dialog", Font.BOLD, 12));
        header.setForeground(TEXT);
        header.setBackground(new Color(241, 245, 249));
        header.setBorder(BorderFactory.createLineBorder(BORDER));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        table.setDefaultRenderer(Object.class, renderer);
        return table;
    }

    private DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private void ajustarColumnas(JTable table) {
        if (table.getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setPreferredWidth(60);
        }
    }

    private String valueAt(JTable table, int modelRow, int column) {
        Object value = table.getModel().getValueAt(modelRow, column);
        return value == null ? "" : value.toString();
    }

    private int parseInt(JTextField field, String name) {
        String value = required(field, name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(name + " debe ser numerico");
        }
    }

    private String required(JTextField field, String name) {
        return required(field.getText(), name);
    }

    private String required(JTextArea area, String name) {
        return required(area.getText(), name);
    }

    private String required(String value, String name) {
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Falta el campo " + name);
        }
        return trimmed;
    }

    private void runAction(String successMessage, Action action) {
        try {
            action.run();
            statusLabel.setText(successMessage);
        } catch (Exception e) {
            statusLabel.setText("Error");
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrar() {
        onClose.run();
        dispose();
        System.exit(0);
    }

    public static void configureLookAndFeel() {
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("TextComponent.arc", 8);
        UIManager.put("Table.showHorizontalLines", true);
    }

    @FunctionalInterface
    private interface Action {
        void run();
    }
}
