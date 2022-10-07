package ca.mcgill.ecse.carshop.view;

import ca.mcgill.ecse.carshop.controller.CarShopController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServiceComboVisualizer extends JPanel {
  private static final long serialVersionUID = 5765666411683246454L;

  // UI elements
  private List<Rectangle2D> rectangles = new ArrayList<Rectangle2D>();
  private static final int LINEX = 75;
  private static final int LINETOPY = 10;
  int lineHeight;
  private static final int RECTWIDTH = 150;
  private static final int RECTHEIGHT = 20;
  private static final int SPACING = 10;
  private static final int MAXNUMBEROFSSERVICESSHOWN = 5;

  // data elements
  private ArrayList<String> combo;
  private HashMap<Rectangle2D, String> services;
  private HashMap<String, String> mandatories;
  private String selectedService;
  private int firstVisibleService;
  private ServicePage page;

  public ServiceComboVisualizer(ServicePage page) {
    super();
    init();
    this.page = page;
  }

  private void init() {
    combo = null;
    services = new HashMap<>();
    selectedService = null;
    firstVisibleService = 0;
    addMouseListener(
            new MouseAdapter() {
              @Override
              public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                for (Rectangle2D rectangle : rectangles) {
                  if (rectangle.contains(x, y)) {
                    selectedService = services.get(rectangle);
                    break;
                  }
                }
                repaint();
                if (selectedService != null)
                  page.setServiceBox(selectedService, mandatories.get(selectedService));

              }
            });
  }

  public void setCombo(ArrayList<String> serviceCombo, ArrayList<String> mandat) {
    this.combo = serviceCombo;
    services = new HashMap<>();
    this.mandatories = new HashMap<>();
    selectedService = null;
    firstVisibleService = 0;

    for (int i = 0; i < serviceCombo.size(); i++) {
      mandatories.put(serviceCombo.get(i), mandat.get(i));
    }

    repaint();
  }

  public void setCombo(String bigCombo) {
    services = new HashMap<>();
    mandatories = new HashMap<>();
    combo = new ArrayList<>();
    selectedService = null;
    firstVisibleService = 0;
    for (int i = 0; i < CarShopController.findCombo(bigCombo).getServices().size(); i++) {
      combo.add(CarShopController.findCombo(bigCombo).getServices().get(i).getService().getName());
      mandatories.put(CarShopController.findCombo(bigCombo).getServices().get(i).getService().getName()
              , Boolean.toString(CarShopController.findCombo(bigCombo).getServices().get(i).getMandatory()));
    }

    repaint();
  }

  public void moveUp() {
    if (firstVisibleService > 0) {
      firstVisibleService--;
      repaint();
    }
  }

  public void moveDown() {
    if (combo != null && firstVisibleService < combo.size() - MAXNUMBEROFSSERVICESSHOWN) {
      firstVisibleService++;
      repaint();
    }
  }

  private void doDrawing(Graphics g) {
    if (combo != null) {
      if (combo.size() == 0) return;
      int number = combo.size();
      if (number > MAXNUMBEROFSSERVICESSHOWN) {
        page.setUpVisible(true);
        page.setDownVisible(true);
        number = MAXNUMBEROFSSERVICESSHOWN;
        if (firstVisibleService < combo.size() - MAXNUMBEROFSSERVICESSHOWN) number++;
      } else {
        page.setUpVisible(false);
        page.setDownVisible(false);
      }

      Graphics2D g2d = (Graphics2D) g.create();
      BasicStroke thickStroke = new BasicStroke(4);
      g2d.setStroke(thickStroke);
      lineHeight = (number - 1) * (RECTHEIGHT + SPACING);
      g2d.drawLine(LINEX, LINETOPY, LINEX, LINETOPY + lineHeight);

      BasicStroke thinStroke = new BasicStroke(2);
      g2d.setStroke(thinStroke);
      rectangles.clear();
      services.clear();
      int index = 0;
      int visibleIndex = 0;
      for (String service : combo) {
        if (index >= firstVisibleService && visibleIndex < MAXNUMBEROFSSERVICESSHOWN) {
          Rectangle2D rectangle =
                  new Rectangle2D.Float(
                          LINEX - RECTWIDTH / 2,
                          LINETOPY - RECTHEIGHT / 2 + visibleIndex * (RECTHEIGHT + SPACING),
                          RECTWIDTH,
                          RECTHEIGHT);
          rectangles.add(rectangle);
          services.put(rectangle, service);

          g2d.setColor(Color.WHITE);
          g2d.fill(rectangle);
          g2d.setColor(Color.BLACK);
          g2d.draw(rectangle);
          g2d.drawString(
                  service,
                  LINEX - RECTWIDTH / 4,
                  LINETOPY + RECTHEIGHT / 4 + visibleIndex * (RECTHEIGHT + SPACING));

          if (selectedService == service) {
            g2d.drawString(
                    mandatories.get(selectedService).toString(),
                    LINEX + RECTWIDTH * 3 / 5,
                    LINETOPY + RECTHEIGHT / 4 + visibleIndex * (RECTHEIGHT + SPACING));
            g2d.setColor(Color.RED);
            g2d.drawString(
                    service,
                    LINEX - RECTWIDTH / 4,
                    LINETOPY + RECTHEIGHT / 4 + visibleIndex * (RECTHEIGHT + SPACING));
          }
          visibleIndex++;
        }
        index++;
      }
    } else {
      page.setUpVisible(false);
      page.setDownVisible(false);
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    doDrawing(g);
  }

  public String getSelectedService() {
    return selectedService;
  }

  public String[] getCombosString() {
    String[] res = new String[combo.size()];
    for (int i = 0; i < combo.size(); i++) {
      res[i] = combo.get(i);
    }
    return res;
  }

  public ArrayList<String> getCombo() {
    return new ArrayList<>(combo);
  }

  public ArrayList<String> getMandatory() {
    ArrayList<String> res = new ArrayList<>();
    for (String s : combo) {
      res.add(mandatories.get(s));
    }
    return res;
  }

  public String[] getMandatoriesString() {
    String[] res = new String[combo.size()];
    for (int i = 0; i < combo.size(); i++) {
      res[i] = mandatories.get(combo.get(i)).toString();
    }
    return res;
  }
}
