import java.util.ArrayList;

public class Calculator {

	private int[] numbers;
	private String expression = "";

	public Calculator(int[] numbers) {
		this.numbers = numbers;
	}

	public void calculate() {
		String[] combination = new String[4];
		String[] operators = {"+", "-", "*", "/"};
		String[][] combinations = generateCombinations(combination, operators);
		for (String[] i : combinations) {
			for (String j : i) {
				System.out.print(j + "");
			}
			System.out.println();
		}
		int[][] permutations = this.permute(this.numbers);
		for (int[] i : permutations) {
			for (int j : i) {
				System.out.print(j + " ");
				this.expression += j;
			}
			System.out.println();
		}
	}

	private int[][] permute(int[] numbers) {
		int[][] permutations = new int[factorial(numbers.length)][];
		int count = 0;
		for (int i = 0; i < numbers.length; i++) {
			int[] permutation = new int[numbers.length - 1];
			int start = 0;
			for (int j = 0; j < numbers.length; j++) {
				if (j == i) {
					start = numbers[j];
				} else if (j > i || j == numbers.length - 1) {
					permutation[j - 1] = numbers[j];
				} else {
					permutation[j] = numbers[j];
				}
			}
			if (permutation.length >= 2) {
				int[][] subPermutations = permute(permutation);
				for (int k = 0; k < subPermutations.length; k++) {
					int[] fullPermutation = new int[permutation.length + 1];
					fullPermutation[0] = start;
					for (int l = 0; l < permutation.length; l++) {
						fullPermutation[l + 1] = subPermutations[k][l];
					}
					permutations[count++] = fullPermutation;
				}
			} else {
				int[] fullPermutation = new int[permutation.length + 1];
				fullPermutation[0] = start;
				for (int j = 0; j < permutation.length; j++) {
					fullPermutation[j + 1] = permutation[j];
				}
				permutations[count++] = fullPermutation;
			}
		}
		return permutations;
	}

	private int factorial(int n) {
		int m = 1;
		for (int i = 1; i <= n; i++) {
			m *= i;
		}
		return m;
	}

	private String[][] generateCombinations(String[] combination, String[] list) {
		String[][] combinations = new String[(int) Math.pow(list.length, combination.length)][combination.length];
		if (combination.length == 1) {
			for (int o = 0; o < 4; o++) {
				combinations[o][0] = list[o];
			}
		} else {
			String[] newCombination = new String[combination.length - 1];
			for (int o = 0; o < list.length; o++) {
				String [][] comb = generateCombinations(newCombination, list);
				for (int i = 0; i < comb.length; i++) {
					String[] fullCombination = new String[comb[i].length + 1];
					fullCombination[0] = list[o];
					for (int j = 0; j < comb[j].length; j++) {
						fullCombination[j + 1] = comb[i][j];
					}
					combinations[comb.length * o + i] = fullCombination;
				}
			}
		}
		return combinations;
	}

	public static void main(String[] args) {
		int[] numbers = new int[4];
		if (args.length != 4) {
			System.out.println("Enter 4 numbers");
		} else {
			for (int i = 0; i < args.length; i++) {
				try {
					numbers[i] = Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {
					System.out.println("Not a number");
				}
			}
		}
		Calculator calculator = new Calculator(numbers);
		calculator.calculate();
	}
}