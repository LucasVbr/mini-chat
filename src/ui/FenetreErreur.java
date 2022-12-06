/*
 * FenetreErreur.java, 06/12/2022
 * INU Champollion, 2022-2023
 * pas de copyright, aucun droits
 */

package ui;

import javax.swing.*;

/**
 * Gestion de l'affichage des erreurs pour les utilisateurs
 *
 * @author Gaël Burguès
 * @author Laurian Dufrechou
 * @author Lucàs Vabre
 */
public class FenetreErreur {

    /**
     * Crée une fenêtre d'erreur avec un message
     *
     * @param message Message d'erreur
     * @param parent  La fenêtre parente
     */
    public FenetreErreur(String message, JFrame parent) {
        final String TITLE = "Erreur";
        JOptionPane.showMessageDialog(parent, message, TITLE, JOptionPane.ERROR_MESSAGE);
    }
}