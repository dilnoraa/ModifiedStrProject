package LazyEditor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import LazyEditor.modifiedStr;

public class file {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		File fileOutput = new File("output.html"); // Bu dosyaya uretilen
		// ciktilar yazilir.
if (!fileOutput.exists()) {
fileOutput.createNewFile();
}
FileWriter fileOutputWriter = new FileWriter(fileOutput, false);
BufferedWriter writer_output = new BufferedWriter(fileOutputWriter);

File fileInput = new File("input.txt"); // input.txt dosyasýndan veriler
	// okunur
if (!fileInput.exists()) {
fileInput.createNewFile();
}
FileReader fileReader = new FileReader(fileInput);
BufferedReader reader_input = new BufferedReader(fileReader);

String line;
String lineCombined = "";// birden fazla satiri birlestirmek için
// kullanilan degisken
int lineFully = 0;

while ((line = reader_input.readLine()) != null || lineFully == 1) {

if (line != null) { // input.txt'te metin bitmediyse...
if (line.trim().length() == 0) { // eger satýr boþsa

if (lineFully == 1) {

lineCombined = line_translate(lineCombined); // satýr
							// html
							// formatýna
							// cevrilir
							// ve
							// direk
							// output
							// dosyasina
							// yazýlýr
writer_output.write(lineCombined);
writer_output.write("\n\n");
lineCombined = "";
lineFully = 0;

}
continue;

} else { // eger satir dolu ýse, bu satir birlesik degiskene
// atanir

lineCombined += line;

lineFully = 1;

}
} else if (lineFully == 1) { // en son satýr icin uygulanan islemler
lineCombined = line_translate(lineCombined);
writer_output.write(lineCombined);
writer_output.write("\n\n");
lineFully = 0;
}

}

reader_input.close();
writer_output.close();

}

private static String line_translate(String lineCombined) {

lineCombined = functionParagraph(lineCombined);// her satýra <p> ve </p>
			// eklenir

String newString = null;

modifiedStr changingString;
int flagForImage = 0;

while (lineCombined.contains("![") && lineCombined.contains("](")
&& flagForImage == 0) { // yaniltici ![ ve ]( parantezler
// buldugu sürece while dongusu calisir
// flagForImage 1e esit oldugunda resim
// icin uygun parantezler bulundu demek
changingString = fonksiyon_kontrol(lineCombined, "![");
lineCombined = changingString.getSentence();
flagForImage = changingString.getFlag();

}
int flagForLink = 0;

while (lineCombined.contains("[") && lineCombined.contains("](")
&& flagForLink == 0 && flagForImage == 0) {
changingString = fonksiyon_kontrol(lineCombined, "[");
lineCombined = changingString.getSentence();
flagForLink = changingString.getFlag();

}

if (flagForImage == 1) { // resim icin yapilan islemler

newString = fonksiyon_stil_isaretciler(lineCombined, "![");
newString = functionImage(newString);

lineCombined = newString;

} else if (flagForLink == 1) { // link icin yapilan islemler

newString = fonksiyon_stil_isaretciler(lineCombined, "[");
newString = functionLink(newString);

lineCombined = newString;
}

else { // eger ![ ]( veya [ ]( parantezler yoksa ...
newString = fonksiyon_isaret_degistir(lineCombined);

newString = functionSlashCall(newString);
lineCombined = newString;

}

System.out.println(lineCombined);
System.out.println("\n");

return lineCombined;
}

private static String fonksiyon_isaret_degistir(String yeniString) {

if (yeniString.contains("***")) {
yeniString = fonksiyonReplace(yeniString, "***", "<h1>");

}
if (yeniString.contains("*")) {
yeniString = fonksiyonReplace(yeniString, "*", "<strong>");

}
if (yeniString.contains("_")) {
yeniString = fonksiyonReplace(yeniString, "_", "<em>");

}// buraya istediðimiz zaman yeni bir iþaretçi ekleyebiliriz

return yeniString;
}

private static String functionSlashCall(String yeniString) {

if (yeniString.contains("<h1>"))
yeniString = functionSlash(yeniString, "<h1>");

if (yeniString.contains("<strong>"))
yeniString = functionSlash(yeniString, "<strong>");

if (yeniString.contains("<em>"))
yeniString = functionSlash(yeniString, "<em>");

return yeniString;
}

private static modifiedStr fonksiyon_kontrol(String lineComposite,
String parantez) { //

int index_1, index_2;
index_1 = lineComposite.indexOf(parantez);
index_2 = lineComposite.indexOf("](", index_1 + 2);

String parca = lineComposite.substring(index_2 + 1);
modifiedStr degisen_string = new modifiedStr();

int bulundu = 0;
degisen_string.setFlag(bulundu); // bulundu 1 oldugunda bu satirda uygun
// parantezler var demek
String yeni_string = null;

if (parca.length() < 8) { // (http://site...).. parcada linkin uzunlugu
// ile diger karakterler dahil
// en az 6-7 olmali
if (parantez == "![")
yeni_string = lineComposite.substring(0, index_1 + 1); // uzunluk
							// 7 den
							// kucukse
							// bu
							// yaniltici
							// parantezdir
							// dolayisiyla
							// strigten
							// [
							// kaldirilir
else if (parantez == "[")
yeni_string = lineComposite.substring(0, index_1);

if (parantez == "![")
yeni_string += lineComposite.substring(index_1 + 2);
else if (parantez == "[")
yeni_string += lineComposite.substring(index_1 + 1);

degisen_string.setSentence(yeni_string); // yaniltici parantezlerin
			// kaldirilimis hali
			// olan string return
			// ile dondurulur
return degisen_string;
} else if (parca.contains(")")) {

int index1, index2;
index1 = index_2 + 1; // ] ve( yan yana olduklari icin '('
// parantezin indeksi hemen sonra
index2 = lineComposite.indexOf(")", index1 + 1); // ) parantezin
					// indeksi
					// bulunur

if (index2 - index1 > 7) { // (http://site...) url nin uzunlugu
// uygunsa bu gercek parantezler o
// yuzden bulundu=1 olur

bulundu = 1;
degisen_string.setFlag(bulundu);
degisen_string.setSentence(lineComposite);

return degisen_string;
} else { // eger url uzunlugu gerektiginden kisa ise bu yaniltici
// parantezler dolayisiyla string degistirilir,[
// karakter silinir

if (parantez == "![")
yeni_string = lineComposite.substring(0, index_1 + 1);

else if (parantez == "[")
yeni_string = lineComposite.substring(0, index_1);

if (parantez == "![")
yeni_string += lineComposite.substring(index_1 + 2);
else if (parantez == "[")
yeni_string += lineComposite.substring(index_1 + 1);
degisen_string.setSentence(yeni_string);

return degisen_string;

}
}

return degisen_string;
}

private static String fonksiyon_stil_isaretciler(String lineComposite,
String parantez) {

String yeniString = null;

int index_1, index_2;
index_1 = lineComposite.indexOf(parantez);
index_2 = lineComposite.indexOf("](", index_1 + 1);

String parca2 = lineComposite.substring(index_2);
if (parca2.contains(")")) {
int index1, index2;

index1 = parca2.indexOf("(");
index2 = parca2.indexOf(")", index1 + 1);

String parca = lineComposite.substring(0, index_1);

parca = fonksiyon_isaret_degistir(parca); // [ veya ![ parantezden
			// onceki kisimda stil
			// ayarlanir

yeniString = parca;
yeniString += lineComposite.substring(index_1, index_2);

parca = lineComposite.substring(index_2);

parca = fonksiyon_isaret_degistir(parca);

yeniString += parca;

yeniString = functionSlashCall(yeniString);

}

return yeniString;
}

private static String functionLink(String line) {

int index_metin1, index_metin2;
index_metin1 = line.indexOf("[");
index_metin2 = line.indexOf("]", index_metin1 + 1);

String link_metni = null;

if (index_metin2 - index_metin1 > 2) { // [...] parantezler arasindaki
	// kismin (link) uzunlugu 2 den
	// buyukse...
link_metni = line.substring(index_metin1 + 1, index_metin2);

}
String new_line = null;
int index_url1, index_url2;

index_url1 = line.indexOf("(");
index_url2 = line.indexOf(")", index_url1 + 1);
String linkUrl_string;
if (index_url2 - index_url1 > 6) { // (.....) parantezler arasindaki
// uzunluk 6 dan buyukse.

linkUrl_string = line.substring(index_url1 + 1, index_url2);

new_line = line.substring(0, index_metin1);
new_line += "<a href=\"";

new_line += linkUrl_string;
new_line += "\">";

new_line += link_metni;
new_line += "</a>";

new_line += line.substring(index_url2 + 1);

}

return new_line;
}

private static String functionParagraph(String line) {

String satir;
satir = "<p>";
satir += line;
satir += "</p>";
return satir;
}

private static String fonksiyonReplace(String line, String degisecek,
String degisen) {

line = line.replace(degisecek, degisen);
return line;
}

private static String functionImage(String line) {

int index_resim1, index_resim2;
index_resim1 = line.indexOf("![");

index_resim2 = line.indexOf("]", index_resim1 + 1);

String resim_alternatif_isim = null;

if (index_resim2 - index_resim1 > 2) {
resim_alternatif_isim = line.substring(index_resim1 + 2,
index_resim2);

}
String new_line = null;
int index_url1, index_url2;
String parca;
parca = line.substring(index_resim2);
index_url1 = parca.indexOf("(");
index_url2 = parca.indexOf(")", index_url1 + 1);
String image_url = null;
if (index_url2 - index_url1 > 3) {

image_url = parca.substring(index_url1 + 1, index_url2);

new_line = line.substring(0, index_resim1);

new_line += "<img src=\"";

new_line += image_url;

new_line += "\" alt=\"";

new_line += resim_alternatif_isim;
new_line += "\">";
new_line += parca.substring(index_url2 + 1);

} else {

new_line = line.substring(0, index_resim1 + 1);
new_line += line.substring(index_resim1 + 2);

}

return new_line;
}

private static String functionSlash(String line, String degisen) {

int index1, index2;

index1 = line.indexOf(degisen);

index2 = line.indexOf(degisen, index1 + 1);

String yeni_string = null;

if (index1 > 0) {
yeni_string = line.substring(0, index1);

}

yeni_string += line.substring(index1, index2 + 1);
yeni_string += "/";

yeni_string += line.substring(index2 + 1); // sonraki stil isaretci
		// ciftleri bulmak icin
		// stringin diger kismi
		// alinir

String yeni_parca = null;

if (yeni_string.length() > index2 + 4)// <...>...<..> karakterlerýn
	// sayýsý 4 ten fazla olmalý
yeni_parca = yeni_string.substring(index2 + 2);

if (yeni_parca.length() > 5 && yeni_parca.contains(degisen)) {
yeni_string = yeni_string.substring(0, index2 + 2);

yeni_string += functionSlash(yeni_parca, degisen); // recursive
					// seklinde
					// stringin
					// diger
					// kisminda
					// isaretci
					// ciftleri
					// aranir

}

return yeni_string;

	}

}
