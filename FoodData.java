package application;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    
    /**
     * Public constructor
     */
    public FoodData() {
        foodItemList = new ArrayList<FoodItem>();
        BPTree<Double, FoodItem> calories = new BPTree<Double, FoodItem>(3);
        BPTree<Double, FoodItem> fat = new BPTree<Double, FoodItem>(3);
        BPTree<Double, FoodItem> carbs = new BPTree<Double, FoodItem>(3);
        BPTree<Double, FoodItem> fiber = new BPTree<Double, FoodItem>(3);
        BPTree<Double, FoodItem> protein = new BPTree<Double, FoodItem>(3);
        indexes = new HashMap<String, BPTree<Double, FoodItem>>();
        indexes.put("calories", calories);
        indexes.put("fat", fat);
        indexes.put("carbohydrate", carbs);
        indexes.put("fiber", fiber);
        indexes.put("protein", protein);
    }
    
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
        Scanner scnr = null;
    	try {
    	File file = new File(filePath);
    	scnr = new Scanner(file);
    	foodItemList.clear();
    	while(scnr.hasNext()) {
    		String arr [] = new String [12];
    		arr = scnr.next().split(",");
    		
    		if (arr != null && arr.length > 0) {
    		FoodItem item = new FoodItem(arr[0], arr[1]);
    		for(int i = 2; i < arr.length; i+=2) {
    			item.addNutrient(arr[i], Double.parseDouble(arr[i+1]));
    		}
    		for(int i = 2; i < arr.length; i+=2) {
    			indexes.get(arr[i]).insert(Double.parseDouble(arr[i+1]), item);
    		}
    		//System.out.print(indexes.get("calories").toString());
    	//System.out.print(indexes.get("carbohydrate").toString());
    		//System.out.print(indexes.get("fat").toString());
    		//System.out.print(indexes.get("protein").toString());
    		//System.out.print(indexes.get("fiber").toString());
    		
    		foodItemList.add(item);
    		
    	}
        }
    	
    	}
        catch(FileNotFoundException except) {
        	System.out.println	(except.getMessage());       
        }
        finally {
        	if (scnr != null)
        	scnr.close();
        }
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
    	List<FoodItem> tempList = new ArrayList<FoodItem>();
        if (substring == null)
        	substring = "";
    	for (FoodItem item: foodItemList) {
    		if (item.getName().toLowerCase().contains(substring.toLowerCase())) {
				tempList.add(item);
			}
    	}
    	
        return tempList;

    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {

    	String arr[] = rules.get(0).split(" ");
    	
    	List<FoodItem> filtered = indexes.get(arr[0]).rangeSearch(Double.parseDouble(arr[1]), arr[2]);
    	
    	
    	List<FoodItem> filtered1 = new ArrayList<FoodItem>();
    	
    	for(int i = 1; i < rules.size(); i++) {
    		arr = rules.get(i).split(" ");
    		filtered1 = indexes.get(arr[0]).rangeSearch(Double.parseDouble(arr[1]), arr[2]);
    		for(int j = 0; j < filtered.size(); j++) {
    			if (!(filtered1.contains(filtered.get(j)))) {
    				filtered.remove(j);
    				j = j-1;
    			}
    		}
    	}
    		
    	    	return filtered;    	
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
        foodItemList.add(foodItem);
        Set<String> nutrients = foodItem.getNutrients().keySet();
        for(String nutrientName : nutrients) {
        	indexes.get(nutrientName).insert(foodItem.getNutrientValue(nutrientName), foodItem);
        }
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }


	@Override
	public void saveFoodItems(String filename) {
		PrintWriter writer = null;
		try {
			 writer = new PrintWriter(filename);
			for(int i = 0; i < foodItemList.size(); i++) {
				String line = "";
				String name = foodItemList.get(i).getName();
				String id = foodItemList.get(i).getID();
				HashMap<String, Double> nutrients = foodItemList.get(i).getNutrients();
				line = id +"," + name + "," + "calories," + nutrients.get("calories") + "," + "fat," + 
				nutrients.get("fat") + "," + "carbohydrate," + nutrients.get("carbohydrate") + "," + "fiber," + 
				nutrients.get("fiber") + "," + "protein," + nutrients.get("protein");
				writer.println(line);
			}
			
		}
		catch(Exception e) {
			System.out.print(e.getMessage());
		}
		finally {
			if (writer != null)
			writer.close();
		}
	}

}
