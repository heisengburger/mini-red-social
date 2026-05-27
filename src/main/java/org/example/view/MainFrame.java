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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private final UsuarioController usuarioController;
    private final PublicacionController publicacionController;
    private final InteresController interesController;
    private final RedSocialController redSocialController;
    private final Runnable onClose;

    private JTable usuariosTable;
    private JTable publicacionesTable;
    private JTable interesesTable;
    private JTable consultasTable;

    public MainFrame(Driver driver, Runnable onClose) {
        this.usuarioController = new UsuarioController(new UsuarioRepository(driver));
        this.publicacionController = new PublicacionController(new PublicacionRepository(driver));
        this.interesController = new InteresController(new InteresRepository(driver));
        this.redSocialController = new RedSocialController(new RedSocialRepository(driver));
        this.onClose = onClose;

        setTitle("Mini Red Social - Neo4j");
        setSize(1050, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                cerrar();
            }
        });

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Usuarios", buildUsuariosPanel());
        tabs.addTab("Publicaciones", buildPublicacionesPanel());
        tabs.addTab("Intereses", buildInteresesPanel());
        tabs.addTab("Relaciones", buildRelacionesPanel());
        tabs.addTab("Consultas", buildConsultasPanel());
        add(tabs, BorderLayout.CENTER);

        cargarTablas();
    }

    private JPanel buildUsuariosPanel() {
        usuariosTable = createTable("ID", "Nombre", "Edad", "Ciudad");

        JTextField idField = new JTextField(8);
        JTextField nombreField = new JTextField(14);
        JTextField edadField = new JTextField(8);
        JTextField ciudadField = new JTextField(14);

        JPanel form = formPanel();
        addField(form, "ID", idField, 0);
        addField(form, "Nombre", nombreField, 1);
        addField(form, "Edad", edadField, 2);
        addField(form, "Ciudad", ciudadField, 3);

        JButton crearButton = button("Crear");
        crearButton.addActionListener(event -> runAction(() -> {
            usuarioController.crearUsuario(
                    parseInt(idField, "ID"),
                    nombreField.getText().trim(),
                    parseInt(edadField, "Edad"),
                    ciudadField.getText().trim()
            );
            cargarUsuarios();
        }));

        JButton actualizarButton = button("Actualizar");
        actualizarButton.addActionListener(event -> runAction(() -> {
            usuarioController.actualizarUsuario(
                    parseInt(idField, "ID"),
                    nombreField.getText().trim(),
                    parseInt(edadField, "Edad"),
                    ciudadField.getText().trim()
            );
            cargarUsuarios();
        }));

        JButton eliminarButton = button("Eliminar");
        eliminarButton.addActionListener(event -> runAction(() -> {
            usuarioController.eliminarUsuario(parseInt(idField, "ID"));
            cargarTablas();
        }));

        JButton recargarButton = button("Recargar");
        recargarButton.addActionListener(event -> cargarUsuarios());

        JPanel buttons = buttonPanel(crearButton, actualizarButton, eliminarButton, recargarButton);
        return section(form, buttons, usuariosTable);
    }

    private JPanel buildPublicacionesPanel() {
        publicacionesTable = createTable("ID", "Texto", "Fecha");

        JTextField autorIdField = new JTextField(8);
        JTextField idField = new JTextField(8);
        JTextArea textoArea = new JTextArea(3, 26);
        JTextField fechaField = new JTextField(12);

        JPanel form = formPanel();
        addField(form, "Autor ID", autorIdField, 0);
        addField(form, "Publicacion ID", idField, 1);
        addArea(form, "Texto", textoArea, 2);
        addField(form, "Fecha", fechaField, 3);

        JButton crearButton = button("Crear");
        crearButton.addActionListener(event -> runAction(() -> {
            redSocialController.crearPublicacion(
                    parseInt(autorIdField, "Autor ID"),
                    parseInt(idField, "Publicacion ID"),
                    textoArea.getText().trim(),
                    fechaField.getText().trim()
            );
            cargarPublicaciones();
        }));

        JButton actualizarButton = button("Actualizar");
        actualizarButton.addActionListener(event -> runAction(() -> {
            publicacionController.actualizarPublicacion(
                    parseInt(idField, "Publicacion ID"),
                    textoArea.getText().trim(),
                    fechaField.getText().trim()
            );
            cargarPublicaciones();
        }));

        JButton eliminarButton = button("Eliminar");
        eliminarButton.addActionListener(event -> runAction(() -> {
            publicacionController.eliminarPublicacion(parseInt(idField, "Publicacion ID"));
            cargarPublicaciones();
        }));

        JButton recargarButton = button("Recargar");
        recargarButton.addActionListener(event -> cargarPublicaciones());

        JPanel buttons = buttonPanel(crearButton, actualizarButton, eliminarButton, recargarButton);
        return section(form, buttons, publicacionesTable);
    }

    private JPanel buildInteresesPanel() {
        interesesTable = createTable("Nombre");

        JTextField nombreField = new JTextField(14);
        JTextField nuevoNombreField = new JTextField(14);

        JPanel form = formPanel();
        addField(form, "Nombre", nombreField, 0);
        addField(form, "Nuevo nombre", nuevoNombreField, 1);

        JButton crearButton = button("Crear");
        crearButton.addActionListener(event -> runAction(() -> {
            interesController.crearInteres(nombreField.getText().trim());
            cargarIntereses();
        }));

        JButton renombrarButton = button("Renombrar");
        renombrarButton.addActionListener(event -> runAction(() -> {
            interesController.actualizarInteres(
                    nombreField.getText().trim(),
                    nuevoNombreField.getText().trim()
            );
            cargarIntereses();
        }));

        JButton eliminarButton = button("Eliminar");
        eliminarButton.addActionListener(event -> runAction(() -> {
            interesController.eliminarInteres(nombreField.getText().trim());
            cargarIntereses();
        }));

        JButton recargarButton = button("Recargar");
        recargarButton.addActionListener(event -> cargarIntereses());

        JPanel buttons = buttonPanel(crearButton, renombrarButton, eliminarButton, recargarButton);
        return section(form, buttons, interesesTable);
    }

    private JPanel buildRelacionesPanel() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel form = formPanel();
        JTextField usuarioIdField = new JTextField(8);
        JTextField amigoIdField = new JTextField(8);
        JTextField desdeField = new JTextField("2025", 8);
        JTextField interesUsuarioIdField = new JTextField(8);
        JTextField interesNombreField = new JTextField(14);
        JTextField likeUsuarioIdField = new JTextField(8);
        JTextField likePublicacionIdField = new JTextField(8);

        addField(form, "Usuario ID", usuarioIdField, 0);
        addField(form, "Amigo ID", amigoIdField, 1);
        addField(form, "Desde", desdeField, 2);
        addField(form, "Usuario interes ID", interesUsuarioIdField, 3);
        addField(form, "Interes", interesNombreField, 4);
        addField(form, "Usuario like ID", likeUsuarioIdField, 5);
        addField(form, "Publicacion like ID", likePublicacionIdField, 6);

        JButton crearAmistadButton = button("Crear amistad");
        crearAmistadButton.addActionListener(event -> runAction(() -> redSocialController.crearAmistad(
                parseInt(usuarioIdField, "Usuario ID"),
                parseInt(amigoIdField, "Amigo ID"),
                desdeField.getText().trim()
        )));

        JButton eliminarAmistadButton = button("Eliminar amistad");
        eliminarAmistadButton.addActionListener(event -> runAction(() -> redSocialController.eliminarAmistad(
                parseInt(usuarioIdField, "Usuario ID"),
                parseInt(amigoIdField, "Amigo ID")
        )));

        JButton asignarInteresButton = button("Asignar interes");
        asignarInteresButton.addActionListener(event -> runAction(() -> {
            redSocialController.asignarInteres(
                    parseInt(interesUsuarioIdField, "Usuario interes ID"),
                    interesNombreField.getText().trim()
            );
            cargarIntereses();
        }));

        JButton likeButton = button("Dar like");
        likeButton.addActionListener(event -> runAction(() -> redSocialController.darLike(
                parseInt(likeUsuarioIdField, "Usuario like ID"),
                parseInt(likePublicacionIdField, "Publicacion like ID")
        )));

        root.add(form, BorderLayout.NORTH);
        root.add(buttonPanel(crearAmistadButton, eliminarAmistadButton, asignarInteresButton, likeButton),
                BorderLayout.CENTER);
        return root;
    }

    private JPanel buildConsultasPanel() {
        consultasTable = createTable("Resultado", "Dato 1", "Dato 2", "Dato 3", "Dato 4");

        JTextField usuarioField = new JTextField("Ana", 12);
        JTextField origenField = new JTextField("Ana", 12);
        JTextField destinoField = new JTextField("Sofia", 12);

        JPanel form = formPanel();
        addField(form, "Usuario", usuarioField, 0);
        addField(form, "Origen", origenField, 1);
        addField(form, "Destino", destinoField, 2);

        JButton sugerenciasButton = button("Amigos sugeridos");
        sugerenciasButton.addActionListener(event -> runAction(() -> mostrarSugerencias(usuarioField.getText().trim())));

        JButton interesesButton = button("Intereses comunes");
        interesesButton.addActionListener(event -> runAction(() -> mostrarInteresesComunes(usuarioField.getText().trim())));

        JButton likesButton = button("Mas likes");
        likesButton.addActionListener(event -> runAction(this::mostrarPublicacionesConLikes));

        JButton caminoButton = button("Camino corto");
        caminoButton.addActionListener(event -> runAction(() ->
                mostrarCamino(origenField.getText().trim(), destinoField.getText().trim())));

        JButton ciudadesButton = button("Ciudades");
        ciudadesButton.addActionListener(event -> runAction(this::mostrarComunidades));

        JPanel buttons = buttonPanel(sugerenciasButton, interesesButton, likesButton, caminoButton, ciudadesButton);
        return section(form, buttons, consultasTable);
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
    }

    private void cargarIntereses() {
        List<Interes> intereses = interesController.listarIntereses();
        DefaultTableModel model = model("Nombre");
        for (Interes interes : intereses) {
            model.addRow(new Object[]{interes.getNombre()});
        }
        interesesTable.setModel(model);
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
    }

    private void mostrarCamino(String origen, String destino) {
        RedSocialRepository.CaminoUsuario camino = redSocialController.buscarCaminoMasCorto(origen, destino);
        DefaultTableModel model = model("Origen", "Destino", "Camino", "Saltos");
        model.addRow(new Object[]{origen, destino, camino.caminoMasCorto(), camino.numeroDeSaltos()});
        consultasTable.setModel(model);
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
    }

    private JPanel section(JPanel form, JPanel buttons, JTable table) {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);
        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        return root;
    }

    private JPanel formPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return panel;
    }

    private void addField(JPanel panel, String label, JTextField field, int row) {
        GridBagConstraints labelConstraints = constraints(0, row);
        labelConstraints.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label, SwingConstants.RIGHT), labelConstraints);

        GridBagConstraints fieldConstraints = constraints(1, row);
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, fieldConstraints);
    }

    private void addArea(JPanel panel, String label, JTextArea area, int row) {
        GridBagConstraints labelConstraints = constraints(0, row);
        labelConstraints.anchor = GridBagConstraints.NORTHEAST;
        panel.add(new JLabel(label, SwingConstants.RIGHT), labelConstraints);

        GridBagConstraints areaConstraints = constraints(1, row);
        areaConstraints.weightx = 1;
        areaConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JScrollPane(area), areaConstraints);
    }

    private GridBagConstraints constraints(int x, int y) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(4, 4, 4, 4);
        return constraints;
    }

    private JPanel buttonPanel(JButton... buttons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        for (JButton button : buttons) {
            panel.add(button);
        }
        return panel;
    }

    private JButton button(String text) {
        JButton button = new JButton(text);
        button.setFocusable(false);
        return button;
    }

    private JTable createTable(String... columns) {
        JTable table = new JTable(model(columns));
        table.setRowHeight(26);
        table.setAutoCreateRowSorter(true);
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

    private int parseInt(JTextField field, String name) {
        String value = field.getText().trim();
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Falta el campo " + name);
        }
        return Integer.parseInt(value);
    }

    private void runAction(Action action) {
        try {
            action.run();
            JOptionPane.showMessageDialog(this, "Operacion completada");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrar() {
        onClose.run();
        dispose();
        System.exit(0);
    }

    @FunctionalInterface
    private interface Action {
        void run();
    }
}
