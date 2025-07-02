import java.awt.Font;
@SuppressWarnings("serial")
public class FIBReleaseDescriptionPanel extends FimansDescriptionPanel {
	
	public FIBReleaseDescriptionPanel(Update update, Font font, Config configFile) {
		super(update, font, configFile);
		
    	this.addSpace(30);
    	
    	this.addTitel(update.toString());
    	
    	String text = "";
    	if (update.getLink() != null) {
    		this.addLink("Download", update.getLink());
    		text += "Oder: ";
		}
    	
    	String path = configFile.pathToFibReleaseFiles + "\\FIB" + update.getVersion();
    	
    	this.addOpenFolderButtonAndCopyableText(text + path, path);
    	
    	this.addSpace(50);
    	
    	this.addTitel(getNbr() + ". Vorbereitung");
    	this.addSpace(10);
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Bibliotheken:");
    	
    	this.addLabel("FA Finanzen-Programmbibliothek:");
    	fibPgmBibTextField = this.addBibField("DSPOBJD OBJ(F1000X) OBJTYPE(*PGM)");
    	
    	this.addLabel("FA Finanzen-Datenbibliothek:");
    	fibDtaBibTextField = this.addBibField("DSPOBJD OBJ(FBUCHPU) OBJTYPE(*FILE)");
    	
    	this.addLabel("FA Finanzen-Quellenbibliothek:");
    	fibSrcBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/QRPGSRC) OBJTYPE(*FILE)");
    	
    	this.addLabel("Bibliothek mit den Daten zu VEDA SE Basis:");
    	assDtaBibTextField = this.addBibField("DSPOBJD OBJ(XPDIVPU) OBJTYPE(*FILE)");
    	
    	this.addLabel("Bibliothek mit den Programmen zu VEDA SE Basis:");
    	assPgmBibTextField = this.addBibField("DSPOBJD OBJ(X1150R) OBJTYPE(*PGM)");
    	
    	this.addSpace(50);
    	
    	this.addLabel("FA Finanzen-Modifikationsbibliothek:");
    	fibModBibTextField = this.addBibField("DSPOBJD OBJ(*ALL/QRPGSRC) OBJTYPE(*FILE)");
    	
    	this.addSpace(50);
    	
    	
    	add1AtNbr(1);
    	addSection_FibInsDirPath();
    	
    	this.addSpace(50);
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " Installierte Versionen überprüfen:");
    	
    	this.addSpace(10);
    	add1AtNbr(2);
    	this.addBoldLabel(getNbr() + " Classic:");
    	this.addButtonAndCopyableText("DSPDTAARA DTAARA(FIBLIB)");
    	this.addSpace(10);
    	add1AtNbr(2);
    	addSubSection_LookUpGraphicVersion();
    	this.addLabel("faFinanzen = FIB");
    	
    	this.addSpace(50);
    	add1AtNbr(2);
    	addSection_JdkVersion();
    	
    	this.addSpace(50);
    	add1AtNbr(2);
    	addSection_osVersion();
    	
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
		
		path = configFile.pathToFibReleaseFiles + "\\FIB" + update.getVersion();
		
		this.addOpenFolderButtonAndCopyableText("Von: " + path, path);
		String date = update.getReleasedate();
    	String newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	
    	String isoVersion = "R_V7R1M0_1";
    	if (update.getVersion() < 5720) isoVersion = "R_V6R1M0_1";
    	
    	String isoName = "FIB" + update.getVersion() + newDate + isoVersion + ".ISO";
    	
		this.addLabel(isoName + " verschieben in zuvor erstellten Ordner");
		
		this.addSpace(20);
		if (update.getVersion() < 5620) {
			this.addLabel("Bei mehreren ISO-Dateien den nächsten Punkt beachten.");
		};
		
		
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
		
		if (update.getVersion() < 5620) {
			this.addSpace(40);
			
			add1AtNbr(1);
			this.addBoldLabel(getNbr() + " Bei 2 ISO-Dateien:");
			this.addLabel("Bei Fibu Releases vor 5620 gibt es 2 ISO-Dateien.");
			this.addLabel("Einfach beide einspielen und dann die erste normal ausführen.");
			this.addLabel("Dann warten, bis Job auf MSGW läuft:");
			path = "/pictures/multi_ISO_Files1.png";
			this.addImage(path);
			this.addLabel("Mit Auswahl 7 kommt folgende Nachricht:");
			path = "/pictures/multi_ISO_Files2.png";
			this.addImage(path);
			this.addLabel("Dann mit F3 verlassen und die zweite ISO-Datei mounten:");
			path = "/pictures/multi_ISO_Files3.png";
			this.addImage(path);
			this.addLabel("Dann im Job wieder mit Auswahl 7 in die Nachricht und mit Antwort „G“ beantworten.");
			this.addLabel("Der Job sollte wieder den Status AKTIV haben.");
		}
		
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
    	this.addSpace(10);
    	this.addLabel("• Journalisierung herunterfahren unter:");
    	this.addLabel("   30.  Sonderfunktionen -> 4.  Journalisierung beenden");
    	this.addButtonAndLabel("Dateibeschreibung zu FBUCHPU anzeigen", new Object[] {"DSPFD FBUCHPU"});
    	this.addButtonAndLabel("Aufzeichnung durch Journal prüfen (Sollte stehen auf Nein):", new Object[] {"Datei wird derzeit aufgezeichn"});
    	path = "/pictures/Journal.png";
    	this.addImage(path);
    	this.addSpace(50);
        
        add1AtNbr(0);
        this.addTitel(getNbr() + ". Installation Classic Teil");
        this.addSpace(10);
        
		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " LODRUN:");
		this.addLabel("Anwendung verlassen!");
		this.addSpace(20);
		this.addButtonAndLabel("In das Laufwerk gehen.", new Object[] {"WRKIMGCLGE IMGCLG(", imgClgNameTextField, ")"});
		this.addLabel("ISO1-Datei mit Auswahl 6 anhängen");
		this.addButtonAndLabel("Datei ausführen mit LODRUN", new Object[] {"LODRUN DEV(", optNameTextField, ")"});
		
		date = update.getReleasedate();
    	newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
		this.addButtonAndLabel("Parameter für erste Seite:", new Object[] {update.getType() + "\n" + Integer.toString(update.getVersion()) + "\n\'" + update.getReleasedate() + "\'\nB\n", assDtaBibTextField, "\n\n\n\n\n'/QOPT/FIB" + update.getVersion() + newDate + "R'"});
		this.addLabel("(Vorsicht bei mehreren Bibliotheken!)");
		path = "/pictures/lodrunFirst.png";
		this.addImage(path);
		this.addButtonAndLabel("Parameter für zweite Seite:",  new Object[] {update.getType() + "\n" + Integer.toString(update.getVersion()) + "\n\'" + update.getReleasedate() + "\'\nB\n\n", fibPgmBibTextField,"\n", fibDtaBibTextField, "\n*NONE \n", fibSrcBibTextField, "\n", assPgmBibTextField, "\n", assDtaBibTextField, "\n0\n0"});
		path = "/pictures/lodrunSercond.png";
		this.addImage(path);
		this.addSpace(50);
		
		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Während der Installation:");
		this.addButtonAndLabel("Job FIB" + update.getVersion() + "R beobachten.", new Object[] {"WRKUSRJOB"});
		
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
		this.addButtonAndLabel("Job FIB" + update.getVersion() + "R mit Auswahl 5 öffnen.", new Object[] {"WRKUSRJOB"});
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
		add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Classic Installation abschließen:");
		this.addSpace(20);
		
		this.addLabel("Neu erstellten Menüpunkt aufrufen unter:");
		this.addLabel("30.  Sonderfunktionen -> Nacharbeiten zu " + update.getVersion());
		this.addLabel("(Befindet sich ganz unten)");
		path = "/pictures/Menu_after_work.png";
		this.addImage(path);
		this.addLabel("Hier drin jeden Untermenüpunkt aufrufen und bestätigen.");
		this.addLabel("(Dabei wird für jeden Aufruf ein Spool erstellt. Diesen auf Fehler überprüfen.");
		this.addButtonAndLabel("Z.B. Alle vom selben Tag mit WRKSPLF, dann Shift + F6 und dann F11)", "WRKSPLF");
		
		
    	this.addSpace(50);
    	
    	add1AtNbr(0);
    	this.addTitel(getNbr() + ". Installation Grafik Teil");
    	this.addSpace(10);
    	
    	add1AtNbr(1);
    	this.addBoldLabel(getNbr() + " SETUP-Programm:");
    	
    	date = update.getReleasedate();
    	newDate = date.substring(6, 10);
    	newDate += date.substring(3, 5);
    	newDate += date.substring(0, 2);
    	
    	path = configFile.pathToFibReleaseFiles + "\\FIB" + update.getVersion();
		text = "FJI_" + update.getVersion() + "_" + newDate + "_R.jar entzippen unter: ";
    	this.addLabel(text);
    	this.addOpenFolderButtonAndCopyableText(path, path);
    	
    	this.addSpace(50);
    	this.addLabel("In diesem \"Setup.Jar\" Ausf\u00FChren");
    	
    	path = "/pictures/FIB_1_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	this.addLabel(new Object[] {"iJUMP2 Ordner angeben (bzw. Ordner mit .jnlp-Datei):"});
    	this.addButtonAndCopyableText(new Object[] {"Pfad: ",  fibInsDirPathTextField}, new Object[] {fibInsDirPathTextField});
    	
    	path = "/pictures/FIB_2_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/FIB_3_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/FIB_4_s.png";
    	this.addImage(path);
    	
    	this.addSpace(30);
    	
    	path = "/pictures/FIB_5_s.png";
    	this.addImage(path);
    	
    	this.addSpace(50);
    	
    	add1AtNbr(1);
    	addSection_CheckForSuccessfulInstallation();
		
		this.addSpace(50);
		add1AtNbr(0);
    	this.addTitel(getNbr() + ". Nacharbeiten");
    	this.addSpace(10);
    	
    	add1AtNbr(1);
    	addSection_ModificationsUpdate(configFile.pathToFibUpdateFiles + "\\FIB" + update.getVersion());
    	
    	this.addSpace(50);
    	add1AtNbr(1);
    	addSection_CompanySpecificRecordTypes();
    	
    	this.addSpace(50);
    	add1AtNbr(1);
		this.addBoldLabel(getNbr() + " Systeme starten:");
		this.addLabel("• TomCat hochfahren");
		addSubSection_tomCatUp();
    	
		this.addSpace(10);
    	this.addLabel("• Journalisierung starten unter:");
    	this.addLabel("   30.  Sonderfunktionen -> 3.  Journalisierung starten");
    	this.addButtonAndLabel("Dateibeschreibung zu FBUCHPU anzeigen", new Object[] {"DSPFD FBUCHPU"});
    	this.addButtonAndLabel("Aufzeichnung durch Journal prüfen (Sollte stehen auf Ja):", new Object[] {"Datei wird derzeit aufgezeichn"});
    	path = "/pictures/Journal.png";
    	this.addImage(path);
    	
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
		return super.toString();
	}
}
