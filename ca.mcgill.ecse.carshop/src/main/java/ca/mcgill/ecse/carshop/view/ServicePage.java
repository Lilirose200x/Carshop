package ca.mcgill.ecse.carshop.view;

import ca.mcgill.ecse.carshop.controller.CarShopController;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import java.awt.*;
import java.util.ArrayList;

public class ServicePage extends JInternalFrame {

  private static final long serialVersionUID = -4426310869335015542L;

  // UI elements
  private JLabel errorMessage;
  // service
  private JComboBox<String> serviceNameTextField;
  private JLabel serviceNameLabel;
  private JButton addServiceButton;
  private JComboBox<String> serviceToggleList;
  private JComboBox<String> availableTechGarage;
  private JLabel garageTechNameLabel;
  private JLabel serviceToggleLabel;
  private JButton deleteServiceButton;
  private JButton editServiceButton;
  private JTextField durationField;
  private JLabel durationLabel;
  // service combo
  private JButton down;
  private JButton up;
  private JLabel serviceComboLabel;
  private JComboBox<String> mainServiceField;
  private JComboBox<String> serviceComboName;
  private JLabel nameLabel;
  private JLabel additionalServiceLabel;
  private JComboBox<String> additionalServiceField;
  private JCheckBox optionalToggle;
  private JLabel optionalLabel;
  private JButton deleteCombo;
  private JButton editCombo;
  private JButton createCombo;
  private JButton addToCombo;
  private JButton removeFromComboButton;
  private ServiceComboVisualizer serviceComboVisualizer;

  // data elements
  private String error = "";
  private String prevServiceName = null;
  private String prevComboName = null;
  private ArrayList<String> servicesTobe = new ArrayList<>();
  private ArrayList<String> mandatories = new ArrayList<>();


  /** Creates new form CarShop */
  public ServicePage() {
    super("Service", false, false, false, false);
    initComponents();
    refreshData();
  }

  /** This method is called from within the constructor to initialize the form. */
  private void initComponents() {
    ((BasicInternalFrameUI)this.getUI()).setNorthPane(null);
    // elements for error message
    errorMessage = new JLabel();
    errorMessage.setForeground(Color.RED);

    // elements for service
    serviceNameTextField = new JComboBox<String>(new String[0]);
    serviceNameTextField.setEditable(true);
    serviceNameLabel = new JLabel();
    serviceNameLabel.setText("Name:");
    addServiceButton = new JButton();
    addServiceButton.setText("Add");
    editServiceButton = new JButton();
    editServiceButton.setText("Edit");
    durationField = new JTextField();
    durationLabel = new JLabel();
    durationLabel.setText("Duration");
    serviceToggleList = new JComboBox<String>(new String[0]);
    serviceToggleLabel = new JLabel();
    serviceToggleLabel.setText("Select service:");
    availableTechGarage = new JComboBox<String>(new String[0]);

    availableTechGarage.addItem("");
    for (String s : CarShopController.getTechniciansName()) availableTechGarage.addItem(s);

    garageTechNameLabel = new JLabel();
    garageTechNameLabel.setText("Garage");
    deleteServiceButton = new JButton();
    deleteServiceButton.setText("Delete");

    // elements for serviceCombo
    up = new JButton();
    down = new JButton();
    up.setText("UP");
    up.setVisible(false);
    down.setText("DOWN");
    down.setVisible(false);
    removeFromComboButton = new JButton();
    removeFromComboButton.setText("Remove from combo");
    serviceComboLabel = new JLabel();
    serviceComboLabel.setText("Main");
    nameLabel = new JLabel();
    nameLabel.setText("Name");
    mainServiceField = new JComboBox<String>(new String[0]);
    serviceComboName = new JComboBox<String>(new String[0]);
    serviceComboName.setEditable(true);
    additionalServiceField = new JComboBox<String>(new String[0]);
    additionalServiceLabel = new JLabel();
    additionalServiceLabel.setText("Service");
    optionalToggle = new JCheckBox();
    optionalLabel = new JLabel();
    optionalLabel.setText("Mandatory");
    deleteCombo = new JButton();
    deleteCombo.setText("Delete");
    editCombo = new JButton();
    editCombo.setText("Edit");
    addToCombo = new JButton();
    addToCombo.setText("Add to combo");
    createCombo = new JButton();
    createCombo.setText("Create");

    serviceComboVisualizer = new ServiceComboVisualizer(this);
    serviceComboVisualizer.setMinimumSize(new Dimension(300, 100));
    // global settings
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setTitle("Service Page");

    // listeners for service
    addServiceButton.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                addServiceButtonActionPerformed(evt);
              }
            });

    deleteServiceButton.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteServiceButtonActionPerformed(evt);
              }
            });
    editServiceButton.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                upadateServiceButtonActionPerformed(evt);
              }
            });

    mainServiceField.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAdditionalService(evt);
              }
            });
    // listener for service combo
    createCombo.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                createComboButtonActionPerformed(evt);
              }
            });

    mainServiceField.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectMainServiceActionPerformed(evt);
              }
            });
    deleteCombo.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteComboButtonActionPerformed(evt);
              }
            });

    removeFromComboButton.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFromComboActionPerformed(evt);
              }
            });
    editCombo.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                editComboActionPerformed(evt);
              }
            });
    addToCombo.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToComboComboButtonActionPerformed(evt);
              }
            });
    up.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
              }
            });

    down.addActionListener(
            new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
              }
            });

    // horizontal line elements
    JSeparator horizontalLineTop = new JSeparator();

    // layout
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    // Layout
    layout.setHorizontalGroup(
            layout
                    .createSequentialGroup()
                    .addGroup(
                            layout
                                    .createParallelGroup()
                                    .addComponent(errorMessage)
                                    .addComponent(horizontalLineTop)
                                    .addGroup(
                                            layout
                                                    .createSequentialGroup()
                                                    .addGroup(
                                                            layout
                                                                    .createParallelGroup()
                                                                    .addComponent(nameLabel)
                                                                    .addComponent(serviceNameLabel)
                                                                    .addComponent(additionalServiceLabel))
                                                    .addGroup(
                                                            layout
                                                                    .createParallelGroup()
                                                                    .addComponent(serviceNameTextField, 200, 200, 400)
                                                                    .addComponent(serviceComboName, 200, 200, 400)
                                                                    .addComponent(additionalServiceField, 200, 200, 400)
                                                                    .addGroup(
                                                                            layout
                                                                                    .createSequentialGroup()
                                                                                    .addComponent(createCombo)
                                                                                    .addComponent(deleteCombo)
                                                                                    .addComponent(editCombo))
                                                                    .addGroup(
                                                                            layout
                                                                                    .createSequentialGroup()
                                                                                    .addComponent(addServiceButton)
                                                                                    .addComponent(deleteServiceButton)
                                                                                    .addComponent(editServiceButton)))
                                                    .addGroup(
                                                            layout
                                                                    .createParallelGroup()
                                                                    .addComponent(garageTechNameLabel)
                                                                    .addComponent(serviceComboLabel)
                                                                    .addComponent(optionalLabel))
                                                    .addGroup(
                                                            layout
                                                                    .createParallelGroup()
                                                                    .addGroup(
                                                                            layout
                                                                                    .createSequentialGroup()
                                                                                    .addComponent(optionalToggle)
                                                                                    .addGap(10)
                                                                                    .addComponent(addToCombo)
                                                                                    .addComponent(removeFromComboButton))
                                                                    .addComponent(availableTechGarage, 120, 120, 200)
                                                                    .addComponent(mainServiceField, 200, 200, 400))
                                                    .addGroup(layout.createParallelGroup().addComponent(durationLabel))
                                                    .addGroup(
                                                            layout
                                                                    .createParallelGroup()
                                                                    .addComponent(durationField, 60, 60, 120))))
                    .addGroup(
                            layout
                                    .createSequentialGroup()
                                    .addGroup(layout.createParallelGroup().addComponent(up).addComponent(down))
                                    .addComponent(serviceComboVisualizer)));

    layout.linkSize(
            SwingConstants.VERTICAL, new java.awt.Component[] {serviceNameTextField, addServiceButton});
    layout.linkSize(
            SwingConstants.VERTICAL,
            garageTechNameLabel, availableTechGarage);
    layout.linkSize(
            SwingConstants.VERTICAL, durationLabel, durationField);
    layout.linkSize(
            SwingConstants.VERTICAL, mainServiceField, serviceComboLabel);
    layout.linkSize(
            SwingConstants.VERTICAL, serviceComboName, nameLabel);
    layout.linkSize(
            SwingConstants.VERTICAL,
            additionalServiceField, additionalServiceLabel);
    layout.linkSize(
            SwingConstants.VERTICAL, optionalLabel, optionalToggle);
    layout.linkSize(SwingConstants.HORIZONTAL, up, down);
    layout.setVerticalGroup(
            layout
                    .createParallelGroup()
                    .addGroup(
                            layout
                                    .createSequentialGroup()
                                    .addComponent(errorMessage)
                                    .addGroup(
                                            layout
                                                    .createParallelGroup()
                                                    .addComponent(serviceComboVisualizer)
                                                    .addGroup(
                                                            layout.createSequentialGroup().addComponent(up).addComponent(down))
                                                    .addGroup(
                                                            layout
                                                                    .createSequentialGroup()
                                                                    .addGroup(
                                                                            layout
                                                                                    .createParallelGroup()
                                                                                    .addComponent(serviceNameLabel)
                                                                                    .addComponent(serviceNameTextField)
                                                                                    .addComponent(garageTechNameLabel)
                                                                                    .addComponent(availableTechGarage)
                                                                                    .addComponent(durationField)
                                                                                    .addComponent(durationLabel))
                                                                    .addGroup(
                                                                            layout
                                                                                    .createParallelGroup()
                                                                                    .addComponent(addServiceButton)
                                                                                    .addComponent(deleteServiceButton)
                                                                                    .addComponent(editServiceButton))
                                                                    .addGroup(
                                                                            layout
                                                                                    .createParallelGroup()
                                                                                    .addComponent(horizontalLineTop))
                                                                    .addGroup(
                                                                            layout
                                                                                    .createParallelGroup()
                                                                                    .addComponent(mainServiceField)
                                                                                    .addComponent(serviceComboLabel)
                                                                                    .addComponent(nameLabel)
                                                                                    .addComponent(serviceComboName))
                                                                    .addGroup(
                                                                            layout
                                                                                    .createParallelGroup()
                                                                                    .addComponent(additionalServiceLabel)
                                                                                    .addComponent(additionalServiceField)
                                                                                    .addComponent(addToCombo)
                                                                                    .addComponent(removeFromComboButton)
                                                                                    .addComponent(optionalToggle)
                                                                                    .addComponent(optionalLabel))
                                                                    .addGroup(
                                                                            layout
                                                                                    .createParallelGroup()
                                                                                    .addComponent(editCombo)
                                                                                    .addComponent(deleteCombo)
                                                                                    .addComponent(createCombo))))));
    pack();
  }

  private void refreshData() {

    // error
    errorMessage.setText(error);
    if (error == null || error.length() == 0) {
      // populate page with data
      // service
      mainServiceField.removeAllItems();
      mainServiceField.addItem("");
      for (String name : CarShopController.getNonComboServices()) {
        mainServiceField.addItem(name);
      }
      serviceNameTextField.removeAllItems();
      serviceNameTextField.addItem("");
      for (String name : CarShopController.getNonComboServices()) {
        serviceNameTextField.addItem(name);
      }

      additionalServiceField.removeAllItems();
      additionalServiceField.addItem("");
      for (String name : CarShopController.getNonComboServices()) {
        if (name != mainServiceField.getSelectedItem()
                && !servicesTobe.contains(CarShopController.findService(name))) {
          additionalServiceField.addItem(name);
        }
      }

      serviceComboName.removeAllItems();
      serviceComboName.addItem("");
      for (String name : CarShopController.getComboServicesName()) {
        serviceComboName.addItem(name);
      }
      availableTechGarage.setSelectedItem("");
      durationField.setText("");
    }
    pack();
  }

  private void addServiceButtonActionPerformed(java.awt.event.ActionEvent evt) {
    // clear error message
    error = "";
    if (addServiceButton.getText().equals("Add")) {
      try {
        if (((String) serviceNameTextField.getSelectedItem()).trim().equals("")
                || ((String) availableTechGarage.getSelectedItem()).trim().equals("")
                || durationField.getText().trim().equals(""))
          throw new RuntimeException("Cells needed to be filled");
        CarShopController.createService(
                (String) serviceNameTextField.getSelectedItem(),
                CarShopController.getChosenGarage((String) availableTechGarage.getSelectedItem()),
                Integer.parseInt(durationField.getText()));
      } catch (Exception e) {
        error = e.getMessage();
      }
    } else {
      addServiceButton.setText("Add");
      editServiceButton.setText("Edit");
    }
    refreshData();
  }

  private void upadateServiceButtonActionPerformed(java.awt.event.ActionEvent evt) {
    // clear error message and basic input validation
    error = "";
    String selectedservice = (String) serviceNameTextField.getSelectedItem();
    try {
      if (selectedservice.trim().equals(""))
        throw new RuntimeException("service needs to be selected to edit it!");
      if (error.length() == 0) {
        if (editServiceButton.getText().equals("Edit")) {
          refreshData();
          serviceNameTextField.setSelectedItem(selectedservice);
          prevServiceName = selectedservice;
          editServiceButton.setText("Save");
          addServiceButton.setText("Cancel");
          availableTechGarage.setSelectedItem(CarShopController.findService(selectedservice).getGarage().getTechnician().getUsername());
          durationField.setText(String.valueOf(CarShopController.findService(selectedservice).getDuration()));
        } else {

          if (CarShopController.findService(prevServiceName).getDuration() != Integer.parseInt(durationField.getText())) {
            CarShopController.updateServiceDuration(
                    selectedservice, Integer.parseInt(durationField.getText()));
          }
          if (!CarShopController.findService(prevServiceName)
                  .getGarage()
                  .getTechnician()
                  .getUsername()
                  .equals(availableTechGarage.getSelectedItem())) {
            CarShopController.updateServiceDuration(
                    selectedservice, Integer.parseInt(durationField.getText()));
          }
          if (!selectedservice.equals(prevServiceName)) {
            CarShopController.updateServiceName(prevServiceName, selectedservice);
          }
          addServiceButton.setText("Add");
          editServiceButton.setText("Edit");
          refreshData();
        }
      }
    } catch (Exception e) {
      error = e.getMessage();
      refreshData();
    }
  }

  private void deleteServiceButtonActionPerformed(java.awt.event.ActionEvent evt) {
    // clear error message and basic input validation
    error = "";
    if (((String) serviceNameTextField.getSelectedItem()).trim().equals("")) {
      error = "Service needed to be chosen";
    }
    for (String name : CarShopController.getComboServicesName()) {
      if (CarShopController.isInCombo(
              (String) (serviceNameTextField.getSelectedItem()), name)) {
        error = "Service is in Combo: " + name;
      }
    }
    if (error.length() == 0) {
      String selectedservice = (String) (serviceNameTextField.getSelectedItem());
      // call the controller
      try {
        CarShopController.deleteService(selectedservice);
      } catch (Exception e) {
        error = e.getMessage();
      }
    }
    refreshData();
  }

  public void createComboButtonActionPerformed(java.awt.event.ActionEvent evt) {
    error = "";
    try {
      if (createCombo.getText().trim().equals("Create")) {
        if (((String) serviceComboName.getSelectedItem()).trim().equals(""))
          throw new RuntimeException("Create a name for combo");
        if (servicesTobe.size() < 2)
          throw new RuntimeException("Combo needs to have > 1 services");
        String[] servicesStr = new String[servicesTobe.size()];
        String[] mandatoryStr = new String[mandatories.size()];
        for (int i = 0; i < servicesStr.length; i++) {
          servicesStr[i] = servicesTobe.get(i);
          mandatoryStr[i] = mandatories.get(i);
        }
        CarShopController.createServiceCombo(
                CarShopController.getCurrentUser().getUsername(),
                servicesStr,
                (String) mainServiceField.getSelectedItem(),
                (String) serviceComboName.getSelectedItem(),
                mandatoryStr);
        serviceComboVisualizer.setCombo((String) serviceComboName.getSelectedItem());
      } else {
        createCombo.setText("Create");
        editCombo.setText("Edit");
      }
    } catch (Exception e) {
      error = e.getMessage();
    }
    refreshComboSection();
  }

  public void removeFromComboActionPerformed(java.awt.event.ActionEvent evt) {
    error = "";
    if (serviceComboVisualizer.getSelectedService() != null) {
      int index = -1;
      for (int i = 0; i < servicesTobe.size(); i++) {
        if (servicesTobe
                .get(i)
                .equals(serviceComboVisualizer.getSelectedService())) {
          index = i;
          break;
        }
      }
      if (index > 0) {
        servicesTobe.remove(index);
        mandatories.remove(index);
        serviceComboVisualizer.setCombo(servicesTobe, mandatories);
      }
    }
    refreshServices();
  }

  public void editComboActionPerformed(java.awt.event.ActionEvent evt) {
    error = "";
    if (!((String) serviceComboName.getSelectedItem()).trim().equals("")) {
      if (editCombo.getText().equals("Edit")) {
        if (!CarShopController.comboExist((String) serviceComboName.getSelectedItem())) return;
        prevComboName = (String) serviceComboName.getSelectedItem();
        editCombo.setText("Save");
        createCombo.setText("Cancel");
        serviceComboVisualizer.setCombo((String) serviceComboName.getSelectedItem());
        servicesTobe = serviceComboVisualizer.getCombo();
        mandatories = serviceComboVisualizer.getMandatory();
        mainServiceField.setSelectedItem(CarShopController.findCombo((String) serviceComboName.getSelectedItem()).getMainService().getService().getName());
      } else {
        editCombo.setText("Edit");
        CarShopController.updateService(
                CarShopController.getCurrentUser().getUsername(),
                prevComboName,
                serviceComboVisualizer.getCombosString(),
                (String) mainServiceField.getSelectedItem(),
                (String) serviceComboName.getSelectedItem(),
                serviceComboVisualizer.getMandatoriesString());
        refreshComboSection();
      }
    }
  }

  public void deleteComboButtonActionPerformed(java.awt.event.ActionEvent evt) {
    // clear error message and basic input validation
    error = "";
    if (((String) serviceComboName.getSelectedItem()).trim().equals("")) {
      error = "Service needed to be chosen";
    }
    if (error.length() == 0) {
      String selectedservice = (String) (serviceComboName.getSelectedItem());
      // call the controller
      try {
        CarShopController.deleteService(selectedservice);
        editCombo.setText("Edit");
        createCombo.setText("Create");
      } catch (Exception e) {
        error = e.getMessage();
      }
    }
    refreshComboSection();
  }

  public void addToComboComboButtonActionPerformed(java.awt.event.ActionEvent evt) {
    error = "";
    try {
      if (((String) additionalServiceField.getSelectedItem()).trim().equals(""))
        throw new RuntimeException("Set a name for service");
      servicesTobe.add((String) additionalServiceField.getSelectedItem());
      mandatories.add(Boolean.toString(optionalToggle.isSelected()));

      serviceComboVisualizer.setCombo(servicesTobe, mandatories);
    } catch (Exception e) {
      error = e.getMessage();
    }
    refreshServices();
  }

  public void selectMainServiceActionPerformed(java.awt.event.ActionEvent evt) {
    // Dont touch, very confusing code
    if (serviceComboName.getSelectedItem() != null)
      if (("").equals(serviceComboName.getSelectedItem())) {
        if (mainServiceField.getSelectedItem() != null
                && !((String) mainServiceField.getSelectedItem()).equals("")) {
          servicesTobe.clear();
          mandatories.clear();
          serviceComboName.setSelectedItem((String) mainServiceField.getSelectedItem() + "-basic");

          servicesTobe.add((String) mainServiceField.getSelectedItem());
          mandatories.add("true");
          serviceComboVisualizer.setCombo(servicesTobe, mandatories);
        }
      } else {
        if (!((String) serviceComboName.getSelectedItem())
                .contains((String) mainServiceField.getSelectedItem())) {
          serviceComboName.setSelectedItem((String) mainServiceField.getSelectedItem() + "-basic");
        }

        int index = -1;
        for (int i = 0; i < servicesTobe.size(); i++) {
          if (servicesTobe
                  .get(i)
                  .equals(
                          CarShopController.findService((String) mainServiceField.getSelectedItem())
                                  .getName())) {
            index = i;
            break;
          }
        }
        if (index > 0) {
          mandatories.remove(index);
          servicesTobe.remove(index);
        }
        try {
          if (!((String) mainServiceField.getSelectedItem()).equals("")) {
            if (servicesTobe.size() == 0 || !servicesTobe.get(0).equals((String) mainServiceField.getSelectedItem())) {
              servicesTobe.add(0, (String) mainServiceField.getSelectedItem());
              mandatories.add(0, "true");
              serviceComboVisualizer.setCombo(servicesTobe, mandatories);
            }
          }
          refreshServices();
        } catch (Exception exception) {
          error = exception.getMessage();
        }
      }
  }
  public void setServiceBox(String service, String mandatory) {
    additionalServiceField.addItem(service);
    additionalServiceField.setSelectedItem(service);
    optionalToggle.setSelected(Boolean.valueOf(mandatory));
  }

  private void refreshComboSection() {
    // for refreshing combo section
    errorMessage.setText(error);
    servicesTobe.clear();
    mandatories.clear();
    up.setVisible(false);
    down.setVisible(false);
    additionalServiceField.setSelectedItem("");
    optionalToggle.setSelected(false);
    serviceComboName.removeAllItems();
    serviceComboName.addItem("");
    mainServiceField.setSelectedItem("");
    for (String name : CarShopController.getComboServicesName()) {
      serviceComboName.addItem(name);
    }
    serviceComboVisualizer.setCombo(new ArrayList<>(), null);
    pack();
  }

  private void refreshServices() {
    // for refreshing service section
    errorMessage.setText(error);
    additionalServiceField.removeAllItems();
    additionalServiceField.addItem("");
    optionalToggle.setSelected(false);
    for (String name : CarShopController.getNonComboServices()) {
      if (name != mainServiceField.getSelectedItem()
              && !servicesTobe.contains(CarShopController.findService(name).getName())) {
        additionalServiceField.addItem(name);
      }
    }
    pack();
  }

  public void setDownVisible(boolean b) {
    this.down.setVisible(b);
  }

  public void setUpVisible(boolean b) {
    this.up.setVisible(b);
  }

  private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {
    serviceComboVisualizer.moveUp();
  }

  private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {
    serviceComboVisualizer.moveDown();
  }

  private void refreshAdditionalService(java.awt.event.ActionEvent evt) {
    refreshServices();
  }
}
