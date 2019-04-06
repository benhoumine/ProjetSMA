package GUI;

import java.awt.GridLayout;
import javax.swing.JSpinner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.SpinnerListModel;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import Village.ElementCarrefour;

/**
*
* @author BENHOUMINE Abdelkhalek & BANE Mamadou
* <p>Classe pour faire les reglages du carrefour , tel que la vitesse et la duree du feu
* 
*/
public class ReglageCarrefour extends javax.swing.JFrame {
	
	private JLabel jLabelID;
	private JLabel jLabelIDText;
	private JSpinner jSpinnerRouge;
	private JButton jButtonAnnuler;
	private JButton jButtonValider;
	private JSpinner jSpinnerVert;
	private JLabel jLabelDureeVert;
	private JSpinner jSpinnerDureeOrange;
	private JLabel jLabelDureeOrange;
	private JLabel jLabelDureeRouge;
	
	private ElementCarrefour leCarrefour;


	
	public ReglageCarrefour(ElementCarrefour leCarrefour) {
		super();
		this.leCarrefour = leCarrefour;
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			GridLayout thisLayout = new GridLayout(5, 2);
			thisLayout.setColumns(2);
			thisLayout.setRows(5);
			this.getContentPane().setLayout(thisLayout);
			this.setTitle("Reglage Carrefour");
			{
				jLabelID = new JLabel();
				this.getContentPane().add(jLabelID);
				jLabelID.setText("Identifiant Carrefour");
			}
			{
				jLabelIDText = new JLabel();
				this.getContentPane().add(jLabelIDText);
				jLabelIDText.setText(new Integer(leCarrefour.getElementVillageCarrefour().getIdentite()).toString());
			}
			{
				jLabelDureeRouge = new JLabel();
				this.getContentPane().add(jLabelDureeRouge);
				jLabelDureeRouge.setText("Duree du rouge");
			}
			{
				SpinnerListModel jSpinnerRougeModel = new SpinnerListModel(
					new String[] { "1", "2", "3", "4", "5", "6",
							"7" });
				jSpinnerRouge = new JSpinner();
				this.getContentPane().add(jSpinnerRouge);
				jSpinnerRougeModel.setValue(new Integer(leCarrefour.getElementVillageCarrefour().getFeuH().getDureeRouge()/10).toString());
				jSpinnerRouge.setModel(jSpinnerRougeModel);
			}
			{
				jLabelDureeOrange = new JLabel();
				this.getContentPane().add(jLabelDureeOrange);
				jLabelDureeOrange.setText("Duree de l'orange");
			}
			{
				SpinnerListModel jSpinnerDureeOrangeModel = new SpinnerListModel(
					new String[] { "1", "2", "3", "4", "5", "6",
							"7" });
				jSpinnerDureeOrange = new JSpinner();
				jSpinnerDureeOrangeModel.setValue(new Integer(leCarrefour.getElementVillageCarrefour().getFeuH().getDureeOrange()/10).toString());
				this.getContentPane().add(jSpinnerDureeOrange);
				jSpinnerDureeOrange.setModel(jSpinnerDureeOrangeModel);
			}
			{
				jLabelDureeVert = new JLabel();
				this.getContentPane().add(jLabelDureeVert);
				jLabelDureeVert.setText("Duree du vert");
			}
			{
				SpinnerListModel jSpinnerVertModel = new SpinnerListModel(
					new String[] { "1", "2", "3", "4", "5", "6",
							"7" });
				jSpinnerVert = new JSpinner();
				jSpinnerVertModel.setValue(new Integer(leCarrefour.getElementVillageCarrefour().getFeuH().getDureeVert()/10).toString());
				this.getContentPane().add(jSpinnerVert);
				jSpinnerVert.setModel(jSpinnerVertModel);
			}
			{
				jButtonValider = new JButton();
				this.getContentPane().add(jButtonValider);
				jButtonValider.setText("Valider");
				jButtonValider.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
				
						leCarrefour.getElementVillageCarrefour().getFeuH().setDureeVert(Integer.parseInt((String) jSpinnerVert.getValue())*10);
						leCarrefour.getElementVillageCarrefour().getFeuV().setDureeVert(Integer.parseInt((String) jSpinnerVert.getValue())*10);											
						
						leCarrefour.getElementVillageCarrefour().getFeuH().setDureeOrange(Integer.parseInt((String) jSpinnerDureeOrange.getValue())*10);
						leCarrefour.getElementVillageCarrefour().getFeuV().setDureeOrange(Integer.parseInt((String) jSpinnerDureeOrange.getValue())*10);
						
						leCarrefour.getElementVillageCarrefour().getFeuH().setDureeRouge(Integer.parseInt((String) jSpinnerRouge.getValue())*10);
						leCarrefour.getElementVillageCarrefour().getFeuV().setDureeRouge(Integer.parseInt((String) jSpinnerRouge.getValue())*10);
						
						setVisible(false);
					}
				});
			}
			{
				jButtonAnnuler = new JButton();
				this.getContentPane().add(jButtonAnnuler);
				jButtonAnnuler.setText("Annuler");
				jButtonAnnuler.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						setVisible(false);
					}
				});
			}
			pack();
			this.setSize(338, 151);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
