package logic;

import java.util.HashSet;
import java.util.LinkedHashMap;

import testing.Testing;

public class CompLogic extends Thread{
    private static int depth = 5;
    private int newColor;

    public CompLogic(){}

    public synchronized void run(){
        // Set played boolean to false for the program 
        // to ignore inputs while computer makes his move
		Logic.setPlayed(false);

        Field[][] board = Logic.getCurrentBoard();
		Field[][] copy  = Util.deepCopy(board);

        int bestEval = this.minimax(copy, CompLogic.depth, -5001, 5001, false, true);
        System.out.println("best evaluation: " + bestEval);
		Logic.setPlayed(true);
        if (this.newColor == 0){
            this.newColor = Util.getRandomColWithExclusion(
                Logic.getPlayerColor(true), Logic.getPlayerColor(false));
        }
		Logic.updatePlayersComponent(this.newColor, false);
    }

    private int minimax(Field[][] board, int depth, 
                        int alpha, int beta, 
                        boolean playerMove, boolean firstMove){
		Field[][] copy  = Util.deepCopy(board);

		Testing test = new Testing(copy);
		
		HashSet<Field> playerComp 	= test.getComponent(copy, test.getNoRows() - 1, 0);
		HashSet<Field> computerComp = test.getComponent(copy, 0, test.getNoColumns() - 1);

        int playersColor = test.getPlayerColor(true, copy);
        int compsColor = test.getPlayerColor(false, copy);

		if(depth == 0 || test.isEndConfig()) {
			return evaluation(playerComp, computerComp, test); 
			// return playerComp.size() - computerComp.size();
		}

		if (playerMove){
			int maxEval = -5000;
			LinkedHashMap<Integer, Integer> availableActions = this.getAvailableActions(copy, playerComp, computerComp,
				playersColor, compsColor);
			// System.out.println("player's available actions: " + availableActions);
			if(!availableActions.isEmpty()){
				for(Integer action: availableActions.keySet()){
					this.transitionModel(playerComp, action);
					int evaluation = this.minimax(test.getBoard(), depth - 1, 
                        alpha, beta, false, false);
					maxEval = evaluation != 5000 ? Math.max(maxEval, evaluation) : maxEval;
					alpha = Math.max(alpha, evaluation);
					if (beta <= alpha) break;
				}
			}
			return maxEval;
		}else{
			int minEval = 5000;
			LinkedHashMap<Integer, Integer> availableActions = this.getAvailableActions(copy, computerComp, playerComp, 
				playersColor, compsColor);
			// System.out.println("comp's available actions: " + availableActions);
			if (!availableActions.isEmpty()){
				for(Integer action: availableActions.keySet()){
					this.transitionModel(computerComp, action);
					int evaluation = this.minimax(test.getBoard(), depth - 1, alpha, beta, true, false);
					if(evaluation < minEval && firstMove) this.newColor = action;

                    minEval = evaluation != -5000 ? Math.min(minEval, evaluation) : minEval;

                    beta = Math.min(beta, evaluation);
					if (beta <= alpha) break;
					
				}
			}
			return minEval;
		}
	}

    private int evaluation(HashSet<Field> playerComp, HashSet<Field> computerComp, Testing test){
		int eval = this.getAdjustedCompSize(playerComp)//  + this.furthestField(playerComp, test, true) 
                 - this.getAdjustedCompSize(computerComp);// - this.furthestField(computerComp, test, false); 
		return eval;
	}

    private int getAdjustedCompSize(HashSet<Field> playerComp){
        int size = 0;
        int noRows = Logic.getNoRows() - 1;
        int noCol = Logic.getNoColumns() - 1;
        for(Field field: playerComp){
            if(field.getCol() == 0 || field.getRow() == 0
            || field.getCol() == noCol || field.getRow() == noRows){
                size += 1;
            }else if(field.getCol() == 1 || field.getRow() == 1
            || field.getCol() == noCol - 1 || field.getRow() == noRows - 1){
                size += 2;
            }else if(field.getCol() == 2 || field.getRow() == 2
            || field.getCol() == noCol - 2 || field.getRow() == noRows - 2){
                size += 3;
            }else{
                size += 4;
            }
        }
        return size;
    }

    private LinkedHashMap<Integer, Integer> getAvailableActions(Field[][] board, HashSet<Field> comp, HashSet<Field> opositeComp, Integer... usedColors){
		LinkedHashMap<Integer, Integer> neighboringColors = this.getNeighbouringColors(board, comp);

		for(Integer usedColor: usedColors){
			neighboringColors.remove(usedColor);
		}
		Util.sortMap(neighboringColors);
		return neighboringColors;
	}

    private LinkedHashMap<Integer, Integer> getNeighbouringColors(Field[][] board, HashSet<Field> comp){
		LinkedHashMap<Integer, Integer> result = new LinkedHashMap<>();
		HashSet<Field> visitedFields = new HashSet<>();

		for(Field field: comp){
			// If field is not in the first row check its upper neighbor
			if(field.getRow() > 0){
				this.updateNeighboringFields(board[field.getRow() - 1][field.getCol()], visitedFields, result);
			}				
			// If field is not in the last row check its lower neighbor
			if(field.getRow() < Logic.getNoRows() - 1){
				this.updateNeighboringFields(board[field.getRow() + 1][field.getCol()], visitedFields, result);
			}
			// If field is not in the first column check its left neighbor
			if(field.getCol() > 0){
				this.updateNeighboringFields(board[field.getRow()][field.getCol() - 1], visitedFields, result);
			}
			// If field is not in the last column check its right neighbor
			if(field.getCol() < Logic.getNoColumns() - 1){
				this.updateNeighboringFields(board[field.getRow()][field.getCol() + 1], visitedFields, result);
			}
		}
		return result;
	}

    private void updateNeighboringFields(Field neighbouringField, HashSet<Field> visitedFields, LinkedHashMap<Integer, Integer> result){
		// If the field has not been already counted, count it
		if(!visitedFields.contains(neighbouringField)){
			visitedFields.add(neighbouringField);
			int color = neighbouringField.getColor();
			int value = result.containsKey(color) ? result.get(color) + 1 : 1;
			result.put(color, value);
		}
	}

    private void transitionModel(HashSet<Field> comp, int color){
		for(Field field: comp){
			field.setColor(color);
		}
	}

    public static int getDepth(){
        return CompLogic.depth;
    }

    public static void setDepth(int depth){
        CompLogic.depth = depth;
    }

    public int getNewColor() {
        return this.newColor;
    }

    public void setNewColor(int newColor) {
        this.newColor = newColor;
    }
}