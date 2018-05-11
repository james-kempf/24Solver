import java.util.ArrayList;
import java.util.Stack;
import java.util.HashSet;
import java.util.TreeSet;
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

		TreeSet<String> solutions = new TreeSet<>();

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
					solutions.add(postfixToInfix(expression));
				}
			}
		}
		// removeDuplicates(solutions);
		if (solutions.isEmpty()) {
			System.out.println("No solutions");
		} else {
			System.out.println(solutions.size() + " Solutions found");
			for (String solution : solutions) {
				System.out.println(solution + " = 24");
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
	 * Recursive method to generate an array of combinations of list of a specified
	 * size
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
	 * Evalutes a prefix expression and returns whether it equals a given sum
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

	/**
	 * Converts a postfix expression to an infix String
	 */
	public String postfixToInfix(String[] postfix) {
		Stack<String> stack = new Stack<>();
		Stack<String> operators = new Stack<>();
		for (String entry : postfix) {
			if (Character.isDigit(entry.charAt(0))) {
				stack.push(entry);
				operators.push("*");
			} else {
				boolean priority = isPriority(entry);
				boolean reversable = isReversable(entry);
				String b = stack.pop();
				String a = stack.pop();
				String bOperator = operators.pop();
				String aOperator = operators.pop();
				boolean reverse = false;
				boolean parA = false;
				boolean parB = false;
				if (!reversable) {
					if (!isPriority(aOperator) && priority) {
						parA = true;
					}
					if (!isNumeric(b)) {
						parB = true;
					}
				} else {
					if (isNumeric(a) && isNumeric(b)) {
						if (Integer.parseInt(b) > Integer.parseInt(a)) {
							reverse = true;
						}
					} else if (b.length() > a.length()) {
						reverse = true;
					}
					if (priority) {
						if (reverse) {
							if (!isPriority(bOperator) && priority) {
								parB = true;
							}
							if (!isPriority(aOperator) || !isReversable(aOperator)) {
								parA = true;
							}
						} else {
							if (!isPriority(aOperator) && priority) {
								parA = true;
							}
							if (!isPriority(bOperator) || !isReversable(bOperator)) {
								parB = true;
							}
						}
					}
				}
				if (parA) {
					a = "(" + a + ")";
				}
				if (parB) {
					b = "(" + b + ")";
				}
				operators.push(entry);
				if (reverse) {
					stack.push(b + " " + entry + " " + a);
				} else {
					stack.push(a + " " + entry + " " + b);
				}
			}
		}
		String infixExpression = stack.pop();
		if (infixExpression.equals("(2 + 1 + 3) * (4)")) {
			for (String i : postfix) {
				System.out.print(i + " ");
			}
			System.out.println();
			System.out.println(infixExpression);
			System.out.println("----------------");
		}
		return infixExpression;
	}

	private boolean isNumeric(String string) {
		try {
			Integer.parseInt(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean isPriority(String operator) {
		return (operator.equals("*") || operator.equals("/"));
	}

	private boolean isReversable(String operator) {
		return (operator.equals("+") || operator.equals("*"));
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
