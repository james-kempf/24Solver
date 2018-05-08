import java.util.ArrayList;

public class Calculator {

	private String[] numbers;
	private String expression = "";

	public Calculator(String[] numbers) {
		this.numbers = numbers;
	}

	public void calculate() {
		String[] operators = {"+", "-", "*", "/"};
		String[][] combinations = generateCombinations(new String[4], operators);
		// for (String[] i : combinations) {
		// 	for (String j : i) {
		// 		System.out.print(j + "");
		// 	}
		// 	System.out.println();
		// }
		String[] test = {"1", "2" ,"3", "4", "5"};
		String[][] perm = permute(test);
		for (String[] i : perm) {
			for (String j : i) {
				System.out.print(j + " ");
			}
			System.out.println();
		}
		System.out.println(perm.length);
		// int[][] permutations = this.permute(this.numbers);
		// for (int[] i : permutations) {
		// 	for (int j : i) {
		// 		System.out.print(j + " ");
		// 		this.expression += j;
		// 	}
		// 	System.out.println();
		// }
		// String[][] expression = new String[factorial(8) * (int) Math.pow(4, 4)][];


		// for (String[] operatorCombination : combinations) {
		// 	String[] expressionArray = new String[8];
		// 	for (int i = 0; i < 4; i++) {
		// 		expressionArray[i] = operatorCombination[i];
		// 		expressionArray[i + 4] = numbers[i];
		// 	}
		// 	String[][] expressions = permute(expressionArray);
		// 	System.out.println(expressions[0][5]);
		// }
	}

	private String[][] permute(String[] list) {
		String[][] permutations = new String[factorial(list.length)][];
		int count = 0;
		for (int i = 0; i < list.length; i++) {
			String[] permutation = new String[list.length - 1];
			String start = list[i];
			for (int j = 0; j < list.length; j++) {
				if (j < i) {
					permutation[j] = list[j];
				} else if (j > i) {
					permutation[j - 1] = list[j];
				}
			}
			if (permutation.length == 1) {
				permutations[count++] = new String[]{start, permutation[0]};
			} else {
				String[][] subPermutations = permute(permutation);
				for (int k = 0; k < subPermutations.length; k++) {
					String[] fullPermutation = new String[permutation.length + 1];
					fullPermutation[0] = start;
					for (int l = 0; l < permutation.length; l++) {
						fullPermutation[l + 1] = subPermutations[k][l];
					}
					permutations[count++] = fullPermutation;
				}
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
					Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {
					System.out.println("Not a number");
				}
			}
		}
		Calculator calculator = new Calculator(args);
		calculator.calculate();
	}
}