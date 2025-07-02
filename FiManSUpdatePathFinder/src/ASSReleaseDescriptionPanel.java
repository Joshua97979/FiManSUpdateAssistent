//By Joshua Froitzheim 2023
import java.awt.Color;
import java.awt.Font;

@SuppressWarnings("serial")
public class ASSReleaseDescriptionPanel extends FimansDescriptionPanel{
	
    public ASSReleaseDescriptionPanel(Update update, Font font, Config configFile) {
    	super(update, font, configFile);
    	
    	this.addSpace(30);
    	
    	this.addTitel("ASS Release " + update.getVersion() + " (" + update.getReleasedate() + ")");
    	
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
    	
    	if (update.getVersion() == 5620) {
    		this.addLabel(new Object[] {"Mit diesem Release muss die propertie JWareHome in:"}, font, Color.red);
    		this.addLabel(new Object[] {"\\iJUMP\\WEB-INF\\classes\\server.properties manuell angepasst werden!"}, font, Color.red);
    		this.addSpace(30);
    	};
    	
    	this.addTitel(getNbr() + ". Vorbereitung");
    	this.addSpace(10);
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Bibliotheken:");
    	
    	this.addLabel("Bibliothek mit den Programmen zu VEDA SE Basis:");
    	assPgmBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/X1150R) OBJTYPE(*PGM)");
    	
    	this.addLabel("Bibliothek mit den Daten zu VEDA SE Basis:");
    	assDtaBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/XPDIVPU) OBJTYPE(*FILE)");
    	
    	this.addLabel("SE-Tools (AES) obj. Dateien-Bibliothek (DTA):");
    	aesDtaBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/V§DDCPU) OBJTYPE(*FILE)");
    	
    	this.addLabel("SE-Tools (AES) obj. Programm-Bibliothek (LIB):");
    	aesPgmBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/CRTDIAPGM) OBJTYPE(*CMD)");
    	
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
    	this.addBoldLabel(getNbr() + " Classic:");
    	this.addButtonAndCopyableText("DSPDTAARA DTAARA(ASSLIB)");
    	this.addSpace(10);
    	add1AtNbr(2);
    	addSubSection_LookUpGraphicVersion();
    	this.addLabel("seBasis = ASS");
    	
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
    	add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Virtuelles Laufwerk einrichten:");
		this.addButtonAndLabel("Bestehendes Laufwerk aussuchen", new Object[] {"WRKDEVD"});
		this.addLabel("(Trotzdem Textfelder unten ausfüllen!)");
		this.addSpace(20);
		this.addLabel("Oder");
		this.addSpace(20);
		this.addLabel("Neues Laufwerk anlegen:");
		optNameTextField = this.addLabelAndTextField("Name des (neuen) Laufwerks:");
		this.addButtonAndLabel("Erstellen mit CRTDEVOPT", new Object[] {"CRTDEVOPT DEVD(", optNameTextField, ") RSRCNAME(*VRT) TYPE(*RSRCNAME) LCLINTNETA(*N)"});
		this.addButtonAndLabel("Anschließend Laufwerkstatus auf AKTIV stellen", new Object[] {"VRYCFG CFGOBJ(", optNameTextField, ") CFGTYPE(*DEV) STATUS(*ON)"});
		
		this.addSpace(40);
		
		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Imagekatalog einrichten:");
		this.addButtonAndLabel("Bestehenden Imagekatalog aussuchen", new Object[] {"WRKIMGCLG"});
		this.addLabel("Mit Auswahl 12 kann man sich den Pfad anzeigen:");
		path = "/pictures/IMGCLG_path.png";
		this.addImage(path);
		this.addLabel("(Trotzdem Textfelder unten ausfüllen!)");
		this.addSpace(20);
		this.addLabel("Oder");
		this.addSpace(20);
		this.addLabel("Neuen Imagekatalog anlegen:");
		this.addLabel("Neuen Ordner anlegen mit Windows-File-Explorer");
		this.addLabel("   (Am besten unter \"/home/VSWAPPL/PTF/NeuerOrdner\")");
		this.addButtonAndLabel("Den relativen Pfad von home aus mit WRKLNK kopieren", new Object[] {"WRKLNK OBJ('/home')"});
		imgClgPathTextField = this.addLabelAndTextField("Relativer Pfad:");
		this.addSpace(20);
		imgClgNameTextField = this.addLabelAndTextField("Name des (neuen) Imagekatalogs:");
		this.addButtonAndLabel("Imagekatalog erstellen mit CRTIMGCLG", new Object[] {"CRTIMGCLG IMGCLG(", imgClgNameTextField, ") DIR('", imgClgPathTextField, "') CRTDIR(*NO)"});
		this.addSpace(40);
		
		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " ISO-Dateien hinzufügen:");
		
		date = update.getReleasedate();
    	newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
		
		path = configFile.pathToAssFiles + "\\AJI" + update.getVersion() + "_" + newDate;
		
		this.addOpenFolderButtonAndCopyableText("Von: " + path, path);
		
    	
    	String isoVersion = "R_V7R1M0_1";
    	if (update.getVersion() < 5710) isoVersion = "R_V6R1M0_1";
    	
    	String isoName = "ASS" + update.getVersion() + newDate + isoVersion + ".ISO";
    	
		this.addLabel(isoName + " verschieben in zuvor erstellten Ordner");
		this.addSpace(20);
		
		this.addButtonAndLabel("Mit ADDIMGCLGE auf AS/400 hinzufügen", new Object[] {"ADDIMGCLGE IMGCLG(", imgClgNameTextField, ") FROMFILE(" + isoName + ") IMGCLGIDX(*AVAIL)"});
		this.addLabel("Eine .ISO01-Datei wurde für die .ISO-Datei erstellt.");
		this.addLabel("Außerdem wird beim ersten Mal eine Textdatei \"QIMGCLG\" einmalig pro Laufwerk erstellt.");
		path = "/pictures/isoFileExplorer.png";
		this.addImage(path);
		this.addSpace(20);
		
		this.addButtonAndLabel("In das Laufwerk gehen.", new Object[] {"WRKIMGCLGE IMGCLG(", imgClgNameTextField, ")"});
		this.addLabel("Im Laufwerk wurde die Datei hinzugefügt.");
		path = "/pictures/isoAS400.png";
		this.addImage(path);
		
		this.addSpace(50);
		
		add1AtNbr(0);
		this.addTitel(getNbr() + ". Installation");
		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Laufwerk Status und Imagekatalog verbinden:");
		
		this.addButtonAndLabel("Ändern mit LODIMGCLG", new Object[] {"LODIMGCLG IMGCLG(", imgClgNameTextField, ") DEV(", optNameTextField, ") OPTION(*LOAD)"});
		this.addButtonAndLabel("In das Laufwerk gehen.", new Object[] {"WRKIMGCLGE IMGCLG(", imgClgNameTextField, ")"});
		this.addLabel("Der Status wurde umgestellt:");
		this.addLabel("(In diesem Status kann man keine Dateien mehr mit Auswahl 4 Löschen oder mit ADDIMGCLGE hinzufügen)");
		path = "/pictures/isoStatus.png";
		this.addImage(path);
		this.addButtonAndLabel("[Der Status kann auch wieder auf \"Nicht Bereit\" geändert werden]", new Object[] {"LODIMGCLG IMGCLG(", imgClgNameTextField, ") DEV(", optNameTextField, ") OPTION(*UNLOAD)"});
    	
		this.addSpace(50);
		
		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Abgeschnittene Dateinamen:");
		this.addLabel("Bei älteren ASS-Releases werden die Dateinamen, ");
		this.addLabel("die durch den LODIMGCLG Befehl aus dem ISO entpackt werden auf 8 Stellen abgeschnitten:");
		path = "/pictures/QOPT_cut_names1.png";
		this.addImage(path);
		this.addLabel("(Vermutlich bei allen ASS-Releases unter 5710 (09.06.2017))");
		
		this.addSpace(20);
		this.addButtonAndLabel("Kann geprüft werden, indem man in das AS/400 Verzeichnis „QOPT“ schaut.", "WRKLNK OBJ('/QOPT')");
		this.addLabel("(Dorthin werden die Dateien entpackt");
		
		this.addSpace(20);
		this.addLabel("Falls dies der Fall ist, legt man im AS/400 IFS einen Ordner an. (Z.B. in tmp/FIB)");
		this.addLabel("Dorthin kopiert man den Inhalt der ISO-Datei (z.B. mit 7zip entpacken):");
		path = "/pictures/QOPT_cut_names2.png";
		this.addImage(path);
		
		this.addSpace(20);
		this.addLabel("Anstatt dem LODRUN-Befehl wird anschließend der CHGPROD-Befehl verwendet!");
		this.addLabel(new Object[] {"Folgende Schritte bitte erst ausführen, wenn normalerweise der LODRUN ausgeführt wird!"}, font, Color.red);
		this.addLabel("Hier die Schritte die später den LODRUN ersetzten:");
		this.addLabel(new Object[] {"Auch hier unbedingt die Anwendung verlassen!"}, font, Color.red);
		this.addLabel("die Bibliothek VEDAINST hinzufügen.");
		this.addButtonAndCopyableText("CHGPROD aufrufen.", "VEDAINST\\CHGPROD");
		this.addLabel("(Achtung, anscheinend löscht er das Objekt CHGPROG bei einem Fehler. Das OBJ besser vorher sichern)");
		
		this.addSpace(20);
		this.addLabel("Erste Seite füllen und den Pfad mit den entpackten Dateien angeben:");
		path = "/pictures/QOPT_cut_names3.png";
		this.addImage(path);
		
		this.addSpace(20);
		this.addLabel("Die nächste Seite ist die erste Seite des LODRUN.");
		this.addLabel("Diese befüllen wie beim LODRUN aber den Pfad anpassen:");
		this.addLabel("(Vorsicht bei mehreren Bibliotheken!)");
		path = "/pictures/QOPT_cut_names4.png";
		this.addImage(path);
		
		this.addLabel("Die zweite Seite wie bei LODRUN befüllen.");
		
		this.addSpace(50);
		
		add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Benutzer abmelden:");
    	this.addButtonAndLabel("Angemeldete Benutzer nachschauen (vor CALL X100SC)", new Object[] {"WRKOBJLCK OBJ(", assDtaBibTextField,"/XPDIVPU) OBJTYPE(*FILE)"});
    	this.addLabel("Dann F6");
    	
    	this.addSpace(10);
    	this.addBoldLabel("Folgende Benutzer mit Auswahl 4 zwangsabmelden:");
    	this.addLabel("• QQF: sind Benutzer in der Grafik");
    	this.addLabel("• FIBASY: ist von einer Buchung und geht nach 30 Min ohne Buchung wieder aus");
    	this.addLabel("   (Muss vor Update aber auch abgemeldet werden)");
    	this.addLabel("• QGENCLT: nicht bekannt aber muss auch abgemeldet werden");
    	this.addLabel("• Benutzer in Grüner Sicht: Fangen bei Kunden mit unterschiedlichen Kürzeln an");
    	this.addLabel("   (Z.B. bei BEM mit WLP)");
        	
    	this.addSpace(20);
    	this.addBoldLabel("FIBASY sollte Status 'END' haben. Prüfen unter:");
    	this.addLabel("30.  Sonderfunktionen -> 1.  Mit asynchronen Jobs arbeiten");
    	
        this.addSpace(50);
    	
        add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Systeme herunterfahren:");
    	this.addLabel("• TomCat herunterfahren");
    	addSubSection_tomCatDown();
    	
    	this.addSpace(50);
    	
    	add1AtNbr(0);
        this.addTitel(getNbr() + ". Installation Classic Teil");
        this.addSpace(10);

		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " LODRUN:");
		this.addLabel(new Object[] {"Anwendung verlassen!"}, font, Color.red);
		this.addSpace(20);
		this.addButtonAndLabel("In das Laufwerk gehen.", new Object[] {"WRKIMGCLGE IMGCLG(", imgClgNameTextField, ")"});
		this.addLabel("ISO1-Datei mit Auswahl 6 anhängen");
		this.addButtonAndLabel("Datei ausführen mit LODRUN", new Object[] {"LODRUN DEV(", optNameTextField, ")"});
        
		date = update.getReleasedate();
    	newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
		this.addButtonAndLabel("Parameter für erste Seite:", new Object[] {update.getType() + "\n" + Integer.toString(update.getVersion()) + "\n\'" + update.getReleasedate() + "\'\nB\n", assDtaBibTextField, "\n\n\n\n\n'/QOPT/ASS" + update.getVersion() + newDate + "'"});
		this.addLabel("(Vorsicht bei mehreren Bibliotheken!)");
		path = "/pictures/lodrunFirstASS.png";
		this.addImage(path);
		this.addButtonAndLabel("Parameter für zweite Seite:",  new Object[] {update.getType() + "\n" + Integer.toString(update.getVersion()) + "\n\'" + update.getReleasedate() + "\'\nB\n\n", assPgmBibTextField,"\n", assDtaBibTextField, "\n", aesPgmBibTextField, "\n", aesDtaBibTextField, "\n0\n0"});
		path = "/pictures/lodrunSercondASS.png";
		this.addImage(path);
		this.addSpace(50);
		
		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Während der Installation:");
		this.addButtonAndLabel("Job ASS" + update.getVersion() + "R beobachten.", new Object[] {"WRKUSRJOB"});
		
		this.addLabel("Dieser sollte bis zum Ende auf AKTIV stehen.");
		this.addSpace(10);
		this.addLabel("Außerdem sollten immer mehr Einträge aufgelistet werden unter:");
		this.addLabel("Auswahl 5 an Job:");
		this.addLabel("• 4. Mit Spool-Dateien arbeiten:");
		path = "/pictures/Spool1.png";
		this.addImage(path);
		
		this.addLabel("• 14. Offene Dateien anzeigen, falls aktiv");
		this.addLabel("   F11:");    		
		path = "/pictures/Spool2.png";
		this.addImage(path);

		this.addSpace(20);
		this.addBoldLabel("Sollte der Job auf MSGW laufen, die folgende Doku beachten:");
		this.addCopyableText("F:\\FiManS Team\\A_Support\\BJU FiBu-Dokus\\LODRUN-Probleme bei Installation Release.docx");
		this.addLabel("(Auswahl 7 um Fehlernachrichten anzuzeigen kann nützlich sein)");
		
		
		this.addSpace(50);
		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Warten, bis der Job auf 'OUTQ' steht.");
		
		this.addSpace(50);

		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Prüfung auf erfolgreiche Installation:");
		this.addButtonAndLabel("Job ASS" + update.getVersion() + "R mit Auswahl 5 öffnen.", new Object[] {"WRKUSRJOB"});
		this.addLabel("4. Mit Spool-Dateien arbeiten");
		this.addSpace(10);
		this.addBoldLabel("Alle Spools überprüfen (Es sollte folgendes am Ende stehen):");
		this.addLabel("• XII10PRT: \"Die Installation wurde erfolgreich beendet!\"");
		this.addLabel("• QPJOBLOG: (ist der letzte Spool) \"Der Beendigungscode des Jobs lautete 0.\"");
		this.addLabel("   \"Der Job endete nach 1 Leitwegschritten mit dem sekundären Beendigungscode 0.\"");
		this.addLabel("   \"Die Jobbeendigungscodes haben folgende Bedeutung:  0\"");
		this.addLabel("• QPQUPRFIL: In diesem Spool werden alle durch das Update reaktivierten Prüfungen aufgelistet.");
		this.addLabel("   (Diese dann wieder auf inaktiv setzten)");
		this.addLabel("• Bei den restlichen Spools wird am Ende bei Erfolg angegeben:");
		this.addLabel("   \"Nicht ges. 0\" oder \"0 nicht zurückgespeichert, 0 ausgeschlossen.\"");
		this.addLabel("   (Bei einigen kann man allerdings gar nicht erkennen, ob diese erfolgreich waren)");
    	
    	this.addSpace(50);
    	
    	add1AtNbr(0);
    	this.addTitel(getNbr() + ". Installation Grafik Teil");
    	this.addSpace(10);
    	
    	if (update.getVersion() == 5620) {
    		this.addLabel(new Object[] {"Mit diesem Release muss die propertie JWareHome in:"}, font, Color.red);
    		this.addLabel(new Object[] {"\\iJUMP\\WEB-INF\\classes\\server.properties manuell angepasst werden!"}, font, Color.red);
    		this.addLabel("Es werden vier neue Properties hinzugefügt. Die letzte dieser Properties heißt JWareHome.");
    		this.addLabel("Dieser Eintrag wird vom Programm mit dem Pfad befüllt, der während der Installation angegeben wurde.");
    		this.addLabel("Beispiel: JWareHome=P:/iJUMP");
    		this.addSpace(10);
    		this.addBoldLabel("Wichtig:");
    		this.addLabel("Der Pfad muss jedoch manuell angepasst werden, sodass er mit /home beginnt.");
    		this.addLabel("Korrektes Beispiel: JWareHome=/home/iJUMP");
    		this.addSpace(30);
    	};
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Setup.JAR Ausf\u00FChren");
    	
    	path = "/pictures/ASS_RW_1_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	this.addLabel("iJUMP2 Ordner angeben (bzw. Ordner mit .jnlp-Datei):");
    	this.addButtonAndCopyableText(new Object[] {"Pfad: ",  fibInsDirPathTextField}, new Object[] {fibInsDirPathTextField}, false, true);
    	
    	path = "/pictures/ASS_RW_2_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	this.addLabel("TomCat Ordner angeben:");
    	this.addButtonAndCopyableText(new Object[] {"Pfad: ", tomCatInsDirPathTextField}, new Object[] {tomCatInsDirPathTextField}, false, true);
    	
    	path = "/pictures/ASS_RW_3_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/ASS_RW_4_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/ASS_RW_5_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/ASS_RW_6_s.png";
    	this.addImage(path);
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	addSection_CheckForSuccessfulInstallation();
    	
    	this.addSpace(50);
    	this.addBoldLabel("TomCat:");
    	this.addLabel("Bei ASS-Releases gibt es neben dem Ordner „j-ware“ häufig auch einen Ordner „Tomcat“ in");
    	this.addLabel("welchem die Änderungen für den Installationsordner des TomCat-Servers liegen.");
    	this.addSpace(10);
    	this.addLabel("Diese müssen auch überprüft werden.");
    	this.addSpace(50);
    	
    	this.addSpace(50);
    	
    	add1AtNbr(0);
    	this.addTitel(getNbr() + ". Systeme starten");
		
    	this.addLabel("• TomCat hochfahren");
		addSubSection_tomCatUp();
    	
    	this.addSpace(20);
    	this.addLabel("• FIBASY starten:");
    	this.addLabel("   Unter: 30.  Sonderfunktionen -> 1.  Mit asynchronen Jobs arbeiten:");
    	this.addLabel("   Auswahl:");
    	this.addLabel("   \"0=Zurücksetzen\"");
    	this.addLabel("   \"1=Starten\"");
    	
    	this.addSpace(20);
    	this.addLabel("(Sollte von „END“ zu *STR zu STR wechseln)");
    	path = "/pictures/FIBASY.png";
    	this.addImage(path);
    	
    	this.addSpace(50);
    }
	
	@Override
	public String toString() {
		return this.toString();
	}
}