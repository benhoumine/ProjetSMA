package TestUnitaire.Listener;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

import Listener.BoutonOutilListener;

public class ListenerTest{

	@Test
	public void testNombreButton() {
		BoutonOutilListener button = new BoutonOutilListener();
		assertTrue(button.getNombreButton()==10);
	}
}
