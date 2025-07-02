import java.awt.Font;

@SuppressWarnings("serial")
public class JAMdescriptionPanel extends FimansDescriptionPanel{
	
    public JAMdescriptionPanel(Update update, Font font, Config configFile) {
    	super(update, font, configFile);
    	
    	this.addSpace(30);
    	
    	this.addTitel(update.toString());
    	
    	String text = "";
    	if (update.getLink() != null) {
    		this.addLink("Download", update.getLink());
    		text += "Oder: ";
		}
    	
    	String date = update.getReleasedate();
    	String newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	String path = configFile.pathToAssFiles + "\\AJI"+ update.getVersion() +"_" + newDate;
    	this.addOpenFolderButtonAndCopyableText(text + path, path);
    	
    	this.addSpace(50);
    	
    	this.addTitel(getNbr() + ". Vorbereitung");
    	this.addSpace(10);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Bibliotheken:");
    	
    	this.addLabel("Bibliothek mit den Daten zu VEDA SE Basis:");
    	
    	assDtaBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/XPDIVPU) OBJTYPE(*FILE)");
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	addSection_FibInsDirPath();
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " TomCat Installationsverzeichnis:");
    	tomCatInsDirPathTextField = this.addLabelAndTextField("Pfad:");
    	this.addLabel("(z.B.: O:\\home\\Tomcat)");
    	this.addButtonAndLabel("Abgleich möglich mit WRKENVVAL unter \"CATAL_HOME\"", new Object[] {assPgmBibTextField, "/WRKENVVAL CFGLIB(", assDtaBibTextField, ") APRGAB(CATAL_HOME)"});
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Installierte Versionen überprüfen");
    	this.addSpace(10);
    	
    	add1AtNbr(2);
    	addSubSection_LookUpGraphicVersion();
    	this.addLabel("System = JUMP");
    	
    	this.addSpace(50);
    	
    	add1AtNbr(2);
    	addSection_tomCatJobName();
    	
    	this.addSpace(50);
    	
    	add1AtNbr(2);
    	addSection_tomCatVersion();
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	addSection_alternativeTomCatCommands();
		
		this.addSpace(50);
    	
    	add1AtNbr(0);
    	this.addTitel(getNbr() + ". Installation");
    	this.addSpace(10);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Benutzer abmelden:");
    	this.addButtonAndLabel("Angemeldete Benutzer nachschauen", new Object[] {"WRKOBJLCK OBJ(", assDtaBibTextField,"/XPDIVPU) OBJTYPE(*FILE)\r\n"});
    	this.addLabel("Dann F6");
    	
    	this.addSpace(10);
    	this.addLabel("QQF-Benutzer mit Auswahl 4 zwangsabmelden:");
    	this.addLabel("(Restliche Benutzer können bei Updates angemeldet bleiben)");

    	this.addSpace(50);
        
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " TomCat herunterfahren");
    	addSubSection_tomCatDown();
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Setup.JAR Ausf\u00FChren");
    	
    	path = "/pictures/JAM_1_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	this.addLabel("iJUMP2 Ordner angeben (bzw. Ordner mit .jnlp-Datei):");
    	this.addButtonAndCopyableText(new Object[] {"Pfad: ",  fibInsDirPathTextField}, new Object[] {fibInsDirPathTextField});
    	
    	path = "/pictures/JAM_2_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	this.addLabel("TomCat Ordner angeben:");
    	this.addButtonAndCopyableText(new Object[] {"Pfad: ", tomCatInsDirPathTextField}, new Object[] {tomCatInsDirPathTextField});
    	
    	path = "/pictures/JAM_3_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/JAM_4_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/JAM_5_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/JAM_6_s.png";
    	this.addImage(path);
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	addSection_CheckForSuccessfulInstallation();

    	this.addSpace(50);
    	this.addBoldLabel("TomCat:");
    	this.addLabel("Bei JAM-Updates gibt es neben dem Ordner „j-ware“ häufig auch einen Ordner „Tomcat“ in");
    	this.addLabel("welchem die Änderungen für den Installationsordner des TomCat-Servers liegen.");
    	this.addSpace(10);
    	this.addLabel("Diese müssen auch überprüft werden.");
    	this.addSpace(50);
    	
    	add1AtNbr(0);
    	this.addTitel(getNbr() + ". Nacharbeiten");
    	this.addSpace(10);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Systeme starten:");
    	this.addLabel("TomCat hochfahren:");
    	addSubSection_tomCatUp();
    	
    	this.addSpace(50);
    }
	
	@Override
	public String toString() {
		return this.toString();
	}
}