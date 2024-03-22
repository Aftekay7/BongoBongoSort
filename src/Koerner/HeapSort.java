package Koerner;

public class HeapSort {

	public static void main(String[] args) {
		int[] test = new int[] { 22, 6, 9, 1, 5, 4, 1, 10, 13, 4, 15, 2, 6 };
		for (int value : test) {
			System.out.print(value + ", ");
		}
		System.out.println();
		heapSort(test);
		for (int value : test) {
			System.out.print(value + ", ");
		}
		System.out.println();
	}

	private static int[] arr;
	private static int firstOutOfHeapIndex;

	public static void heapSort(int[] arr) {
		HeapSort.arr = arr;
		firstOutOfHeapIndex = arr.length;
		/*
		 * Erzeuge einen Max-Heap
		 */
		for (int index = (arr.length >> 1) - 1; index >= 0; index--) {
			updateHeap(index, arr[index]);
		}
		while (firstOutOfHeapIndex > 0) {
			/*
			 * Extrahiere das aktuell größte Element des Heaps und füge es
			 * in das sortierte Array ein.
			 */
			int value = arr[--firstOutOfHeapIndex];
			arr[firstOutOfHeapIndex] = arr[0];
			updateHeap(0, value);
		}
	}

	private static void updateHeap(int index, int value) {
		for (;;) {
			int childIndex = (index << 1) + 1;
			if (childIndex >= firstOutOfHeapIndex) {
				break;
			}
			if (childIndex + 1 < firstOutOfHeapIndex) {
				if (arr[childIndex + 1] > arr[childIndex]) {
					childIndex++;
				}
			}
			if (value >= arr[childIndex]) {
				break;
			}
			arr[index] = arr[childIndex];
			index = childIndex;
		}
		arr[index] = value;
	}
}
