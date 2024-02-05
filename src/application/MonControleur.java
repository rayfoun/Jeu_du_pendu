package application;


import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class MonControleur {
	//Attributs
	
	private HashSet<Lettre> mot;	//cette liste contient toutes les lettres du mot à deviner.
	private StringBuffer chaineTrouvee;	//Contient la chaine à deviner
	
	private static boolean tiret = false;
	
	private ArrayList<Character> listeCaracteresTirees = new ArrayList<Character>();
	
	private static int erreur = 0;
	
	private final Path imagesFolderPath = FileSystems.getDefault().getPath("C:\\Users\\Elise ATE\\Desktop\\développement\\workspace\\TP10_Jeu_du_pendu\\src\\application\\images"); // Le chemin vers le dossier d'images
	
	String imageName;
	
	@FXML
	ImageView imageView;
	
	@FXML
	TextField chaine;
	
	@FXML
	TextField car;
	
	@FXML
	Text lettreTiree;
	
	@FXML
	private void initialize() {
		String[] listeMots = {"voiture", "moto", "camion", "train", "bicyclette"};
		Random rand = new Random();
		int alea = rand.nextInt(5);
		String word = listeMots[alea];
		System.out.println(word);
		mot = new HashSet<Lettre>();
		for(int i=0; i<word.length(); i++) {
			Lettre l = new Lettre(word.charAt(i), word);
			mot.add(l);
		}
		chaineTrouvee = new StringBuffer();
		for(int j=0; j<word.length(); j++) {
			if(j == word.length()-1) {
				chaineTrouvee.append("_");
			}
			else {
				chaineTrouvee.append("_ ");
			}
		}
		chaine.setText(chaineTrouvee.toString());
		chaine.setStyle("-fx-alignment: CENTER;");
		car.setStyle("-fx-alignment: CENTER;");
		chaine.setDisable(true);

	}
	
	
	
	@FXML
	private void actionValiderLettre(ActionEvent evt) throws Exception{
		
		String tfcar = "";
		try {
			tfcar = car.getText();
			if(tfcar == "") {
				throw new Exception();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Veuiller entrer un caractère dans le champ");
			return;
		}
		boolean lettreTrouvee = false;  // Permet de contenir le nombre de saut d'espaces
		char[] charArray = tfcar.toCharArray();  //Permet de convertir un objet string en un tableau de caractères
		if(charArray.length != 1) {
			car.setText("");	//On vérifie si le tableau de caractères est bien égal à un et sinon on sort de la fonction
			System.out.println("Vous devez entrer un seul caractère");
			return;
		}
		else {
			try {
				for(char c: listeCaracteresTirees) {
					if(c == charArray[0]) {
						throw new Exception();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Vous avez déjà tiré cette lettre");
				return;
			}
			listeCaracteresTirees.add(charArray[0]);
			for(Lettre l: mot) {   //On parcourt les lettres du mots
				if(charArray[0] == l.getLettre()) {	//On vérifie si le caractère saisit correspond à une lettre du mot
					for(int i: l.getPositions()) {  //Si oui on parcourt les positions de la lettre dans le mot et on remplie chaineTrouvee en ajoutant esp pour gérer l'espace créé
						chaineTrouvee.setCharAt(i*2, charArray[0]);
					}
					lettreTrouvee = true;
				}
			}
			if(lettreTrouvee == false) {
				erreur ++;
				switch(erreur) {
					case 1 : 
						imageName = "pendu_1.jpg";
						break;
					case 2 :
						imageName = "pendu_2.jpg";
						break;
					case 3 :
						imageName = "pendu_3.jpg";
						break;
					case 4 :
						imageName = "pendu_4.jpg";
						break;
					case 5 : 
						imageName = "pendu_5.jpg";
						break;
					case 6 : 
						imageName = "pendu_6.jpg";
						break;
				}
				if(tiret == false) {
					String concat = lettreTiree.getText() + " " + car.getText();
					lettreTiree.setText(concat);
					tiret = true;
				}
				else {
					String concat = lettreTiree.getText() + "-" + car.getText();
					lettreTiree.setText(concat);
				}
				Path imagePath = imagesFolderPath.resolve(imageName);
				Image imageToShow = new Image(imagePath.toUri().toString());
				imageView.setImage(imageToShow);
				
			}
		}
		chaine.setText(chaineTrouvee.toString());
		
		int gagne = 0;
		for(int i=0; i<chaineTrouvee.length(); i++) {
			if(chaineTrouvee.charAt(i) == '_') {
				gagne ++;
			}
		}
		if(gagne == 0) {
			Alert dialog = new Alert(AlertType.CONFIRMATION);
			dialog.setTitle("FIN DE PARTIE");
			dialog.setHeaderText(null);
			dialog.setContentText("Vous avez gagné !!");
			dialog.showAndWait();
		}
		
		if(erreur == 6) {
			Alert dialog = new Alert(AlertType.CONFIRMATION);
			dialog.setTitle("FIN DE PARTIE");
			dialog.setHeaderText(null);
			dialog.setContentText("Vous avez perdu !!");
			dialog.showAndWait();
		}
		
	}
	
}
