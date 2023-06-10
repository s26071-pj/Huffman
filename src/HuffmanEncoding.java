import java.util.*;


//Klasa reprezentująca węzeł w drzewie Huffmana
class Node implements Comparable<Node> {
    // znak
    char character;
    //częstotliwość występowania kodu
    int frequency;
    //lewe i prawe dziecko
    Node left, right;

    public Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public Node(char character, int frequency, Node left, Node right) {
        this.character = character;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    //porównanie węzłów względem ich częstotliwości
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.frequency, other.frequency);
    }
}

public class HuffmanEncoding {
    public static void main(String[] args) {
        // Przyjmowanie tekstu od użytkownika
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj tekst do zakodowania: ");
        String text = scanner.nextLine();

        // Usuwanie spacji i znaków niebędących literami alfabetu
        text = text.replaceAll("\\s+", "");
        text = text.replaceAll("[^a-zA-Z]+", "");

        // Tworzenie tabeli częstości występowania znaków
        Map<Character, Integer> frequencies = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }

        // Budowanie drzewa Huffmana
        PriorityQueue<Node> minHeap = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            minHeap.offer(new Node(entry.getKey(), entry.getValue()));
        }

        while (minHeap.size() > 1) {
            Node leftChild = minHeap.poll();
            Node rightChild = minHeap.poll();
            Node parent = new Node('\0', leftChild.frequency + rightChild.frequency, leftChild, rightChild);
            minHeap.offer(parent);
        }
        //zapisanie utworzonych węzłów drzewa w zmiennej root
        Node root = minHeap.poll();

        // Generowanie kodów Huffmana
        Map<Character, String> codes = new HashMap<>();
        generateCodes(root, "", codes);

        // Wyświetlanie wyników(znaki z ich częstotliwością występowania i kodami Huffmana)
        System.out.println("Znak\tCzęstość\tKod Huffmana");
        for (Map.Entry<Character, Integer> entry : frequencies.entrySet()) {
            char character = entry.getKey();
            int frequency = entry.getValue();
            String code = codes.get(character);
            System.out.println(character + "\t" + frequency + "\t\t" + code);
        }
    }

    // Metoda rekurencyjna do generowania kodów Huffmana
    private static void generateCodes(Node node, String code, Map<Character, String> codes) {
        if (node == null) {
            return;
        }

        //Jeśli dotrzemy do liścia, zapisujemy kod w mapie codes
        if (node.left == null && node.right == null) {
            codes.put(node.character, code);
        }

        generateCodes(node.left, code + "0", codes);
        generateCodes(node.right, code + "1", codes);
    }
}
