import java.util.ArrayList;
import java.util.Stack;
import java.util.HashSet;
import java.util.EmptyStackException;

/**
 * Receives a list of 4 numbers and computes all possible expressions that
 * equals 24
 */
public class Calculator {

	private String[] numbers;

	public Calculator(String[] numbers) {
		this.numbers = numbers;
	}

	/**
	 * Driver method to call all other methods
	 */
	public void calculate() {
		String[] operators = { "+", "-", "*", "/" };
		HashSet<String[]> combinations = generateCombinations(operators, 3);

		HashSet<String[]> solutions = new HashSet<>();

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
		removeDuplicates(solutions);
		if (solutions.isEmpty()) {
			System.out.println("No solutions");
		} else {
			for (String[] solution : solutions) {
				for (String entry : solution) {
					System.out.print(entry + " ");
				}
				System.out.println("= 24");
			}
		}
	}

	/**
	 * Recursive method to generate a HashSet of all permutations of list
	 */
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

	/**
	 * Recursive method to generate an array of combinations of list with a
	 * scpecified size
	 */
	private HashSet<String[]> generateCombinations(String[] list, int choose) {
		HashSet<String[]> combinations = new HashSet<>();
		if (choose == 1) {
			for (String choice : list) {
				combinations.add(new String[] { choice });
			}
		} else {
			HashSet<String[]> subCombinationSet = generateCombinations(list, choose - 1);
			for (String choice : list) {
				for (String[] subCombination : subCombinationSet) {
					String[] fullCombination = new String[subCombination.length + 1];
					fullCombination[0] = choice;
					for (int i = 0; i < subCombination.length; i++) {
						fullCombination[i + 1] = subCombination[i];
					}
					combinations.add(fullCombination);
				}
			}
		}
		return combinations;
	}

	/**
	 * Evalutes a prefix expression, represented by an array of Strings, and returns
	 * whether it equals a given sum
	 */
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

	private void removeDuplicates(HashSet<String[]> expressionSet) {
		int e1 = 0;
		HashSet<String[]> remove = new HashSet<>();
		for (String[] expression1 : expressionSet) {
			e1++;
			int e2 = 0;
			loop: for (String[] expression2 : expressionSet) {
				e2++;
				if (e1 < e2) {
					for (int i = 0; i < expression1.length; i++) {
						if (!expression1[i].equals(expression2[i])) {
							break loop;
						}
					}
					for (String a : expression1) {
						System.out.print(a + " ");
					}
					System.out.print("= ");
					for (String a : expression2) {
						System.out.print(a + " ");
					}
					System.out.println();
					remove.add(expression2);
				}
			}
		}
		expressionSet.removeAll(remove);
	}

	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.println("Please enter 4 numbers");
			System.exit(0);
		} else {
			for (String arg : args) {
				try {
					Integer.parseInt(arg);
				} catch (NumberFormatException e) {
					System.out.println(arg + " - Not a number");
					System.exit(0);
				}
			}
		}
		Calculator calculator = new Calculator(args);
		calculator.calculate();
	}
}
