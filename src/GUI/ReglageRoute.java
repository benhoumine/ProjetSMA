package GUI;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* 
* 
*/

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.WindowConstants;

import Village.ElementRoute;


public class ReglageRoute extends javax.swing.JFrame {
	private JLabel labelVitesse;
	private JSpinner jSpinnerVitesse;
	private JButton boutonValider;
	private JButton boutonAnnuler;
	private JLabel jLabelIDValeur;
	private JLabel jLabelIDRoute;
	
	private ElementRoute laRoute;

	
	public ReglageRoute(ElementRoute route) {
		super();
		this.laRoute = route;
		initGUI();
	}
	
	private void initGUI() {
		try {
			GridLayout thisLayout = new GridLayout(3, 2);
			thisLayout.setColumns(2);
			thisLayout.setRows(3);
			this.getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Reglages Route");
			{
				jLabelIDRoute = new JLabel();
				this.getContentPane().add(jLabelIDRoute);
				jLabelIDRoute.setText("Identifiant Route");
				jLabelIDRoute.setSize(150, 16);
			}
			{
				jLabelIDValeur = new JLabel();
				this.getContentPane().add(jLabelIDValeur);
				jLabelIDValeur.setText(new Integer(laRoute.getElementVillageRoute().getID()).toString());
			}
			{
				labelVitesse = new JLabel();
				this.getContentPane().add(labelVitesse);
				labelVitesse.setText("Vitesse Maximale");
				labelVitesse.setSize(95, 16);
			}
			{
				SpinnerListModel jSpinnerVitesseModel = new SpinnerListModel(
					new String[] { "0", "1", "2", "3", "4", "5",
							"7" });
				jSpinnerVitesseModel.setValue(new Integer(laRoute.getElementVillageRoute().getVitesseAgree()).toString());
				jSpinnerVitesse = new JSpinner();
				this.getContentPane().add(jSpinnerVitesse);
				jSpinnerVitesse.setModel(jSpinnerVitesseModel);
				
				jSpinnerVitesse.getEditor().setSize(50, 16);
			}
			{
				boutonValider = new JButton();
				this.getContentPane().add(boutonValider);
				boutonValider.setText("Valider");
				boutonValider.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						laRoute.getElementVillageRoute().setVitesseAgree(Integer.parseInt((String) jSpinnerVitesse.getValue()));
						setVisible(false);
					}
				});
			}
			{
				boutonAnnuler = new JButton();
				this.getContentPane().add(boutonAnnuler);
				boutonAnnuler.setText("Annuler");
				boutonAnnuler.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						setVisible(false);
					}
				});
			}
			pack();
			this.setSize(242, 107);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
