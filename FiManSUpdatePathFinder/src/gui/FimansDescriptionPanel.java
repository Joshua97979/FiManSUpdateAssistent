package gui;
import java.awt.Font;

import javax.swing.JLabel;

import src.Config;
import src.Update;

@SuppressWarnings("serial")
public class FimansDescriptionPanel extends DescriptionPanel{
	
	public FimansDescriptionPanel(Update update ,Font font, Config configFile) {
		super(update, font, configFile);
	}

	public void addSection_FibInsDirPath() {
		this.addBoldLabel(getNbr() + " Fibu Installationsverzeichnis:");
    	fibInsDirPathTextField = this.addLabelAndTextField("Pfad:");
    	this.addLabel("(z.B.: O:\\home\\iJUMP)");
    	this.addButtonAndLabel("Kann abgeglichen werden mit WRKENVVAL unter \"JMPROOT\"", new Object[] {assPgmBibTextField, "/WRKENVVAL CFGLIB(", assDtaBibTextField, ") APRGAB(JMPROOT)"});
	}
	
	
	public void addSection_tomCatJobName() {
		this.addBoldLabel(getNbr() + " TomCat-Job-Name:");
    	this.addButtonAndLabel("Finden mit WRKENVVAL unter \"JMPTOMJOB\"", new Object[] {assPgmBibTextField, "/WRKENVVAL CFGLIB(", assDtaBibTextField, ") APRGAB(JMPTOMJOB)"});
    	tomCatJobNameTextField = this.addLabelAndTextField("TomCat-Job-Name:");
    	this.addButtonAndLabel(new Object[] {"Ansonsten aktuell laufende TomCats anschauen."}, new Object[] {"WRKACTJOB JOB(IJ*)"});
    	this.addBoldLabel("Bei mehreren TomCat-Jobs:");
    	this.addLabel("Zuerst wählt man an einem aktuell laufenden TomCat-Job die Auswahl 5,");
    	this.addLabel("anschließend 4 („Mit Spool-Dateien arbeiten“) und schließlich 5 bei QPRINT.");
    	this.addSpace(10);
    	this.addLabel("Dadurch erscheint in der ersten Zeile der Pfad zum TomCat-Installationsordner.");
    	this.addLabel("In diesem Verzeichnis öffnet man die Datei \"iJUMP.xml\", die beispielsweise unter");
    	this.addCopyableText("\"\\home\\veda\\Tomcat\\conf\\Catalina\\localhost\\iJUMP.xml\" zu finden ist.");
    	this.addSpace(10);
    	this.addLabel("In der XML-Datei wird der Pfad zum Anwendungs-Installationsordner,");
    	this.addLabel("dem sogenannten \"IJUMP-Ordner\", angezeigt.");
    	this.addLabel("Enthält der Unterordner Client\\lib des IJUMP-Ordners Fibu-Dateien,");
    	this.addLabel("so ist der richtige Fibu-TomCat-Ordner und damit der entsprechende Fibu-TomCat-Job gefunden.");
	}
	
	public void addSection_tomCatVersion() {
		this.addBoldLabel(getNbr() + " TomCat-Version:");
    	this.addLabel("Zu finden:");
    	this.addButtonAndLabel("TomCat-Installationsordner finden mit WRKENVVAL unter \"CATAL_HOME\"", new Object[] {assPgmBibTextField, "/WRKENVVAL CFGLIB(", assDtaBibTextField, ") APRGAB(CATAL_HOME)"});
    	this.addLabel("(Vorsicht, bei mehreren TomCat-Jobs");
    	this.addLabel("bitte stattdessen die obere Anleitung unter \"Bei mehreren TomCat-Jobs:\" befolgen)");
    	this.addSpace(20);
    	this.addLabel("Kopieren und Entpacken von:");
    	this.addBoldLabel("*TomCat-Installationsordner* \\lib\\Catalina.jar");
    	this.addSpace(20);
    	this.addLabel("In entpackter Datei: ");
    	this.addBoldLabel("\\org\\apache\\catalina\\util\\ServerInfo.properties");
    	this.addLabel("In properties:");
    	this.addBoldLabel("server.info=Apache Tomcat/[Version]");
    	tomCatVersionTextField = this.addLabelAndTextField("TomCat-Version:");
    	this.addLabel("(Unter Version 8 kann kein ASS 58.20 installiert werden!)");
	}
	
	public void addSection_alternativeTomCatCommands() {
    	this.addBoldLabel(getNbr() + " Abweichende TomCat-Befehle (falls vorhanden):");
    	this.addLabel("Zu finden:");
    	this.addButtonAndLabel("Geplante Jobs mit WRKJOBSCDE", new Object[] {"WRKJOBSCDE"});
    	this.addLabel("Nach passenden Job Namen suchen (z.B. FIBGUISTR)");
    	this.addLabel("Auswahl 5");
    	this.addLabel("Unter \"Befehl\" findet man das Programm (immer ein CL-Programm):");
    	String path = "/pictures/ScheduledJob.png";
		this.addImage(path);
		this.addLabel("(Oftmals liegt das Programm in der JMPSYSLIB)");
		this.addSpace(10);
		this.addBoldLabel("Bei mehreren TomCat-Jobs:");
    	this.addLabel("Hier hilft es den „Aktuellen Benutzer“ von dem TomCat-Job");
    	this.addLabel("mit dem angegebenen Benutzer beim Startbefehl zu vergleichen,");
		this.addLabel("da dieser pro TomCat-Job unterschiedlich ist (bisher immer):");
		path = "/pictures/jmpsvrUsers.png";
		this.addImage(path);
		this.addSpace(10);
		this.addLabel("Wenn dieser nicht direkt im STRJMPSVR-Befehl enthalten ist,");
		this.addLabel("wird dieser vermutlich im WRKJOBSCDE als Parameter mitgegeben:");
		path = "/pictures/strjmpsvrParams.png";
		this.addImage(path);
		this.addSpace(10);
		
		this.addButtonAndLabel("In dem Programm nach ENDJMPSVR suchen", new Object[] {"ENDJMPSVR"});
		tomCatDownTextField = this.addLabelAndTextField("Abweichender Befehl zum TomCat herunterfahren:");
    	this.addSpace(30);
    	this.addButtonAndLabel("Das gleiche mit dem STRJMPSVR (anderer Job / Programm wahrscheinlich)", new Object[] {"STRJMPSVR"});
    	tomCatUpTextField = this.addLabelAndTextField("Abweichender Befehl zum TomCat hochfahren:");
    	this.addLabel("(Gegebenenfalls AUTOUPD(*YES) auf *NO ändern)");
	}
	
	public void addSubSection_tomCatDown() {
		this.addButtonAndLabel("Abweichender Befehl zum TomCat herunterfahren", new Object[] {tomCatDownTextField});
    	this.addLabel("Ansonsten");
    	this.addButtonAndLabel("Standartbefehl für TomCat herunterfahren", new Object[] {"ENDJMPSVR CFGLIB(", assDtaBibTextField, ")"});
    	this.addButtonAndLabel(new Object[] {"TomCat-Status nachschauen (", tomCatJobNameTextField, "sollte jetzt nicht mehr angezeigt werden)"}, new Object[] {"WRKACTJOB JOB(IJ*)"});
	}
	
	public void addSubSection_tomCatUp() {
    	this.addButtonAndLabel("Abweichender Befehl zum TomCat hochfahren", new Object[] {tomCatUpTextField});
    	this.addLabel("Ansonsten");
    	this.addButtonAndLabel("Standartbefehl für TomCat hochfahren", new Object[] {"STRJMPSVR ACSSVR(*NO) CFGLIB(", assDtaBibTextField, ") USER(ASRPGM)"});
    	this.addLabel("(Benutzer kann unterschiedlich sein!)");
    	this.addButtonAndLabel(new Object[] {"TomCat-Status nachschauen (", tomCatJobNameTextField, "vernünftig hochgefahren?)"}, new Object[] {"WRKACTJOB JOB(IJ*)"});
	}
	
	public void addSection_ModificationsUpdate(String path) {
		this.addBoldLabel(getNbr() + " Modifikationen anheben:");
    	this.addLabel("Dazu in der Doku:");
    	this.addOpenFolderButtonAndCopyableText(path, path);
    	this.addLabel("Unterpunkte beachten unter:");
    	this.addButtonAndCopyableText("\"2 Übersicht der im Update enthaltenen Objekte und Quellen\"", "2 Übersicht der im Update enthaltenen Objekte und Quellen");
    	this.addCopyableText(new Object[] {"Anheben in: ", fibModBibTextField}, true);
	}
	
	public void addSection_CompanySpecificRecordTypes () {
		this.addBoldLabel(getNbr() + " Firmen spezifische Satzarten anheben:");
		this.addLabel("Über die folgenden SQL-Abfragen können die Satzarten nachgeschaut werden, welche Firmenabhängig sind:");
		this.addButtonAndLabel("• Kopfsätze (SQL-Anweisung)", "SELECT XKANWG, XKSA, XKFIRM FROM XKDIVPU\nWHERE XKANWG = 'FIB' AND XKFIRM IN (SELECT FIFIRM FROM FFIRMPU)\nGROUP BY XKANWG, XKSA, XKFIRM ORDER BY XKSA");
		this.addButtonAndLabel("• Positionssätze (SQL-Anweisung)", "SELECT XPANWG, XPSA, XPFIRM FROM XPDIVPU\nWHERE XPANWG = 'FIB' AND XPFIRM IN (SELECT FIFIRM FROM FFIRMPU)\nGROUP BY XPANWG, XPSA, XPFIRM ORDER BY XPSA");
		this.addSpace(10);
		this.addLabel("Die Reinfolge ist:");
		this.addLabel("Anwendungsgebiet, Satzart, Firma");
		this.addSpace(10);
		this.addLabel("Die gefundenen Satzarten vergleichen mit „Version FIB“");
	}
	
	public void addSection_CheckForSuccessfulInstallation() {
		this.addBoldLabel(getNbr() + " Prüfung auf erfolgreiche Installation:");
		this.addLabel("Als erstes kann man die Versionsnummer im GreenScreen und/oder in der version.properties");
		this.addLabel("überprüfen und nachschauen, ob das Programm diese mit der neuen Nummer ausgetuscht hat.");
		this.addSpace(50);
		
		this.addBoldLabel("Bei der Installation macht das Programm manchmal Fehler:");
		this.addLabel("• Neue Dateien werden nicht erstellt");
		this.addLabel("• Dateien oder ZIP-Files werden als Ordner angelegt:");
		this.addImage("/pictures/folder.png");
		this.addLabel("• Alte Dateien werden nicht gelöscht:");
		this.addImage("/pictures/Error_WEBINF_lib.png");
		this.addSpace(50);
		
		this.addBoldLabel("Die folgenden Ordner sind betroffen:");
		this.addLabel("• WEB-INF/lib: werden teilweise die alten jar-Dateien nicht gelöscht");
		this.addLabel("• Client/lib: werden teilweise die alten jar-Dateien nicht gelöscht");
		this.addLabel("• resources/context/Formulare: Es werden Ordner anstelle der Bilder erstellt");
		this.addLabel("• resources/context/Druckerformulare: Es werden Ordner anstelle der PDFs erstellt");
		this.addLabel("• resources/downloads: Es werden Ordner anstelle der ZIPs erstellt");
		this.addSpace(50);
		
		this.addBoldLabel("Vergleich:");
		this.addLabel("In der entpackten Update-Datei (über welche man das Grafik-Update startet)");
		this.addLabel("befindet sich ein Ordner mit dem Namen des Updates.");
		this.addSpace(10);
		this.addLabel("In diesem befindet sich wiederum ein Ordner mit dem Namen „j-ware“.");
		this.addLabel("Hier befinden sich alle Dateien, welche bei diesem Update ausgetauscht, gelöscht oder hinzugefügt werden.");
		this.addSpace(10);
		this.addLabel("Der Aufbau der Ordnerstruktur sollte dabei identisch zu der beim Installations-Ziel sein.");
		this.addLabel("z.B. Client/lib");
		this.addSpace(50);
		
		this.addLabel("Dateien mit der Endung „.delete“ sollen beim Ziel-Ordner gelöscht werden:");
		this.addImage("/pictures/delete.png");
		this.addSpace(50);
		
		this.addBoldLabel("Kommt man in die Grafik?");
		this.addLabel("(Erst nach TomCat Start)");
	}
	
	public void addSection_osVersion() {
		this.addBoldLabel(getNbr() + " Betriebssystem-Version:");
    	osTextField = this.addButtonAndTextField("DSPDTAARA DTAARA(QSS1MRI)");
    	this.addLabel("(Bei allem unter V6R1M0 sollte man hellhörig werden)");
	}
	
	public void addSection_JdkVersion() {
		this.addBoldLabel(getNbr() + " JDK-Version (wegen DSPF):");
    	this.addButtonAndLabel("Finden mit WRKENVVAL unter \"JAVA_HOME\"", new Object[] {assPgmBibTextField, "/WRKENVVAL CFGLIB(", assDtaBibTextField, ") APRGAB(JAVA_HOME)"});
    	jdkTextField = this.addLabelAndTextField("JDK:");
    	this.addLabel("(Bei allem unter jdk80/64bit sollte man hellhörig werden)");
	}
	
	public void addSubSection_LookUpGraphicVersion() {
		this.addBoldLabel(getNbr() + " Grafik:");
    	Object[] objArr = new Object[] {fibInsDirPathTextField, "\\resources\\version.properties"};
    	this.addOpenFolderButtonAndCopyableText(objArr, objArr);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
	public JLabel[] getAllJLabels() {
		return super.getAllJLabels();
	}
}
