package logic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class Util {
    public static Field[][] deepCopy(Field[][]original){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(original);
            out.close();

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            Field[][] copiedArray = (Field[][]) in.readObject();
            in.close();

            return copiedArray;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LinkedHashMap<Integer, Integer> sortMap(LinkedHashMap<Integer, Integer> map){
		LinkedHashMap<Integer, Integer> result = new LinkedHashMap<>();
		ArrayList<Entry<Integer, Integer>> list = new ArrayList<>(map.entrySet());
		list.sort(Comparator.comparing(Entry::getValue, Comparator.reverseOrder()));

		for(Entry<Integer, Integer> entry: list)
			result.put(entry.getKey(), entry.getValue());

		return result;
	}

    /**
	 * Returns random number within the range excluding numbers from @param exclude
	 * Taken from https://stackoverflow.com/questions/6443176/how-can-i-generate-a-random-number-within-a-range-but-exclude-some
	 * 
	 * @param exclude - numbers that should be excluded
	 * @return random number within the range excluding numbers from @param exclude
	 */
	public static int getRandomColWithExclusion(Integer... exclude) {
		int start = 1;
		int end = Logic.getNoColors();
		SortedSet<Integer> set = new TreeSet<Integer>();
		Collections.addAll(set, exclude);
		Random rnd = new Random();
	    int random = start + rnd.nextInt(end - start + 1 - set.size());
	    for (int ex : set) {
	        if (random < ex) break;
	        random++;
	    }
	    return random;
	}
}
