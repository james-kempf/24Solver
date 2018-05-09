import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.HashSet;

public class Calculator {

	private String[] numbers;

	public Calculator(String[] numbers) {
		this.numbers = numbers;
	}

	public void calculate() {
		String[] operators = { "+", "-", "*", "/" };
		String[][] combinations = generateCombinations(new String[3], operators);

		ArrayList<String[]> solutions = new ArrayList<>();

		for (String[] operatorCombination : combinations) {
			String[] expressionArray = new String[7];
			for (int i = 0; i < operatorCombination.length; i++) {
				expressionArray[i] = operatorCombination[i];
			}
			for (int i = 0; i < numbers.length; i++) {
				expressionArray[i + operatorCombination.length] = numbers[i];
			}
			HashSet<String[]> expressions = permute(expressionArray);
			for (String[] expression : expressions) {
				if (compute(expression, 24.0)) {
					solutions.add(expression);
				}
			}
		}
		for (String[] solution : solutions) {
			for (String entry : solution) {
				System.out.print(entry + " ");
			}
			System.out.println("= 24");
		}
	}

	private HashSet<String[]> permute(String[] list) {
		HashSet<String[]> permutations = new HashSet<>();
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
				permutations.add(new String[] { start, permutation[0] });
			} else {
				HashSet<String[]> subPermutations = permute(permutation);
				for (String[] subPermutation : subPermutations) {
					String[] fullPermutation = new String[subPermutation.length + 1];
					fullPermutation[0] = start;
					for (int l = 0; l < subPermutation.length; l++) {
						fullPermutation[l + 1] = subPermutation[l];
					}
					permutations.add(fullPermutation);
				}
			}
		}
		return permutations;
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
				String[][] comb = generateCombinations(newCombination, list);
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

	private boolean compute(String[] expression, double sum) {
		Stack<Double> stack = new Stack<>();
		for (String entry : expression) {
			if (Character.isDigit(entry.charAt(0))) {
				stack.push(Double.parseDouble(entry));
			} else {
				try {
					double b = stack.pop();
					double a = stack.pop();
					switch (entry) {
					case "+":
						stack.push(a + b);
						break;
					case "-":
						stack.push(a - b);
						break;
					case "*":
						stack.push(a * b);
						break;
					case "/":
						try {
							stack.push(a / b);
						} catch (ArithmeticException e) {
							return false;
						}
						break;
					}
				} catch (EmptyStackException e) {
					return false;
				}
			}
		}
		try {
			return (stack.pop() == sum);
		} catch (EmptyStackException e) {
			return false;
		}
	}

	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.println("Enter 4 numbers");
		} else {
			for (int i = 0; i < args.length; i++) {
				try {
					Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {
					System.out.println("Not a number");
					System.exit(0);
				}
			}
		}
		Calculator calculator = new Calculator(args);
		calculator.calculate();
	}
}