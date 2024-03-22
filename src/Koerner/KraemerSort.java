package Koerner;

import Util.Helpers;

public class KraemerSort {

	private static int partitionCount = 0;

	public static void main(String[] args) {
		/*
		 * Die Fibonacci-Zahlen bilden eine ungünstige Verteilung der
		 * Zahlengrößen. Die meisten Fibonacci-Zahlen landen im ersten
		 * Intervall, von diesen bei den rekusriven Aufrufen wiederum die
		 * meisten im ersten Intervall, usw.
		 * Wir berechnen die ersten 47 Fibonacci--Zahlen F_0 bis
		 * F_46. Die 47.-igste Fibonacci-Zahl F_47 ist größer als 2^31 und
		 * passt nicht mehr in ein Java-int.
		 */
		final int size = 47;
		int fibonacci[] = new int[size];
		fibonacci[0] = 0;
		fibonacci[1] = 1;
		for (int index = 2; index < size; index++) {
			fibonacci[index] = fibonacci[index - 1] + fibonacci[index - 2];
		}

		//sortArray(fibonacci);
		/*
		 * Ausgabe des (sowieso schon vorab sortierten) Arrays
		*/
		//System.out.println(fibonacci.length);
		for (int value : fibonacci) {
			//System.out.print(value + ", ");
		}

		Helpers.shuffleArray(fibonacci);
		int[] arr2 = fibonacci.clone();
		Helpers.printArr(fibonacci);
		Tests.compareOnArray(fibonacci);
		//sortArray(fibonacci);
		//LinearScalingSort.sort(arr2);

	}

	/**
	 * Sortiert das übergebene Array. Die Übergabe von null führt zu einer
	 * Exception!
	 * 
	 * @param arr
	 *            Das zu sortierende Array - es darf nicht null sein (wohl aber
	 *            leer).
	 */
	public static void sortArray(int[] arr) {
		KraemerSort.arr = arr;
		temp = new int[arr.length];
		sortPartialArray(0, arr.length);
		//System.out.println(partitionCount);
	}

	/**
	 * Das zu sortierende Feld.
	 */
	private static int[] arr;

	/**
	 * Temporär speichern wir die Werte auch nochmals in diesem Feld.
	 */
	private static int[] temp;

	/**
	 * Sortiert einen Bereich von arr
	 * 
	 * @param begin
	 *            Der zu sortierende Bereich beginnt bei diesem Index.
	 * @param end
	 *            Der zu sortierende Bereich endet bei end - 1, d.h. end ist der
	 *            Index der ersten Zahl außerhalb des Bereichs.
	 */
	private static void sortPartialArray(int begin, int end) {
		/*
		 * Gebe die Größe des ersten Intervalls aus - nur für die Überlegungen
		 * zur Performance des Verfahrens.
		 */
		if (begin == 0) {
			//System.out.println("Das erste Intervall reicht von 0 bis einschließlich " + (end - 1) + ".");
		}
		/*
		 * Wenn das Feld nur höchstens ein Element enthält, ist es bereits
		 * sortiert.
		 */
		if (begin + 1 >= end) {
			return;
		}
		/*
		 * Suche das Minimum und Maximum im übergebenen Bereich heraus
		 */
		int min = arr[begin];
		int max = min;
		for (int index = begin + 1; index < end; index++) {
			if (min > arr[index]) {
				min = arr[index];
			} else if (max < arr[index]) {
				max = arr[index];
			}
		}
		/*
		 * Sind min und max identisch, so haben alle Werte im Array den gleichen
		 * Wert. Natürlich ist das Array dann bereits sortiert.
		 */
		if (min == max) {
			return;
		}
		final int valueRange = max - min + 1;
		final int destRange = end - begin;
		int[] copyIndex = new int[destRange];
		/*
		 * Schritt 1: Wir skalieren jeden Wert im Bereich zwischen min und max
		 * linear auf einen Wert zwischen 0 und destRange - 1 und zählen in
		 * copyIndex[k] mit, wie oft jeder skalierte Wert k vorkommen wird.
		 */
		for (int index = begin; index < end; index++) {
			/*
			 * Die folgende Rechnung wird im long-Bereich durchgeführt. Sonst
			 * kann es bei der inneren Multiplikation zu einem int-Überlauf
			 * kommen.
			 * Da valueRange den Wert den max - min + 1 enthält, ist der Wert
			 * arr[index] - min immer kleiner als dieser Wert, der Quotient also
			 * kleiner als 1 und damit scaledValue immer kleiner als destRange.
			 */
			int scaledValue = (int) (((long) (arr[index] - min)) * destRange / valueRange);
			copyIndex[scaledValue]++;
		}
		/*
		 * Schritt 2: Wir berechnen in copyIndex[k] den ersten Index für das
		 * temp-Array, ab dem die Werte mit dem skalierten Wert k später
		 * abgespeichert werden.
		 */
		int offset = 0;
		for (int index = 0; index < destRange; index++) {
			int newOffset = offset + copyIndex[index];
			copyIndex[index] = offset;
			offset = newOffset;
		}
		/*
		 * Schritt 3: Wie im Schritt 1 gehen wir wieder alle Werte durch, jetzt
		 * speichern wir sie aber auch ab. Durch die berechneten copyIndizes
		 * ist sichergestellt, dass sich die Bereiche nicht überschneiden
		 * werden.
		 */
		for (int index = begin; index < end; index++) {
			/*
			 * Die folgende Rechnung wird im long-Bereich durchgeführt. Sonst
			 * kann es bei der inneren Multiplikation zu einem int-Überlauf
			 * kommen.
			 */
			int value = arr[index];
			int scaledValue = (int) (((long) (value - min)) * destRange / valueRange);
			temp[copyIndex[scaledValue]++] = value;
		}
		/*
		 * Schritt 4: Wir kopieren die unterteilten Bereiche in das
		 * Original-Feld zurück.
		 */
		int sourceIndex = 0;
		for (int index = begin; index < end; index++) {
			arr[index] = temp[sourceIndex++];
		}
		partitionCount++;
		/*
		 * Schritt 5: Wir sortieren die unterteilten Bereiche rekursiv. Für
		 * den k-ten Bereich enthält copyIndex[k] jetzt den ersten Index hinter
		 * diesem Bereich.
		 */
		int beginIndex = begin;
		for (int index = 0; index < destRange; index++) {
			int endIndex = begin + copyIndex[index];
			sortPartialArray(beginIndex, endIndex);
			beginIndex = endIndex;
		}
	}
}
