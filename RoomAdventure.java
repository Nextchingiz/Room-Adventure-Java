import java.util.Scanner; // Import Scanner for reading user input

public class RoomAdventure { // Main class containing game logic

    // class variables
    private static Room currentRoom; // The room the player is currently in
    private static String[] inventory = {null, null, null, null, null}; // Player inventory slots
    private static String status; // Message to display after each action
    private static boolean chestOpened = false; // Check if the chest in Room 4 has been opened, it can only be opened once

    // constants
    final private static String DEFAULT_STATUS =
        "Sorry, I do not understand. Try [verb] [noun]. Valid verbs include 'go', 'look', 'take', and 'open'."; // Default error message

    // Handle functions
    
    // Handle Go
    private static void handleGo(String noun) { // Handles moving between rooms
        String[] exitDirections = currentRoom.getExitDirections(); // Get available directions
        Room[] exitDestinations = currentRoom.getExitDestinations(); // Get rooms in those directions
        status = "I don't see that room."; // Default if direction not found
        for (int i = 0; i < exitDirections.length; i++) { // Loop through directions
            if (noun.equals(exitDirections[i])) { // If user direction matches
                currentRoom = exitDestinations[i]; // Change current room
                status = "Changed Room"; // Update status
            }
        }
    }

    // Handle Look
    private static void handleLook(String noun) { // Handles inspecting items
        String[] items = currentRoom.getItems(); // Visible items in current room
        String[] itemDescriptions = currentRoom.getItemDescriptions(); // Descriptions for each item
        status = "I don't see that item."; // Default if item not found
        for (int i = 0; i < items.length; i++) { // Loop through items
            if (noun.equals(items[i])) { // If user-noun matches an item
                status = itemDescriptions[i]; // Set status to item description
            }
        }
    }

    // Handle Take
    private static void handleTake(String noun) { // Handles picking up items
        String[] grabbables = currentRoom.getGrabbables(); // Items that can be taken
        status = "I can't grab that."; // Default if not grabbable
        for (String item : grabbables) { // Loop through grabbable items
            if (noun.equals(item)) { // If user-noun matches grabbable
                for (int j = 0; j < inventory.length; j++) { // Find empty inventory slot
                    if (inventory[j] == null) { // If slot is empty
                        inventory[j] = noun; // Add item to inventory
                        status = "Added it to the inventory"; // Update status
                        break; // Exit inventory loop
                    }
                }
            }
        }
    }

    // Handle Open (New Feature)
    private static void handleOpen(String noun) { // Handles opening objects
        if (noun.equals("chest") && currentRoom.getName().equals("Room 4")) { // Only works on the chest in Room 4
            if (!chestOpened) { // If chest has not been opened yet
                chestOpened = true; // Set the value to true
                // Automatically add key to the player's inventory
                for (int j = 0; j < inventory.length; j++) { // Check the inventory and if it is full
                    if (inventory[j] == null) { // If there is an empty space
                        inventory[j] = "key"; // Add the key to the inventory
                        status = "You opened the chest and found a key! It's been added to your inventory.";
                        break; // Exit loop
                    }
                }
                if (status == null) { // Alternative route, if the inventory is full
                    status = "You opened the chest and found a key, but your inventory is full!";
                }
            } else { // If the chest has been already opened
                status = "The chest is already open.";
            }
        } else { // If the player tries to open something else
            status = "I can't open that.";
        }
    }

    // Draw Map (New Function)
    private static void drawMap() { // Draws the game map with the player's position as {+}
        System.out.println("\n**********************"); // Top
        // Top (Rooms 1 and 2)
        System.out.print("| Room 1  || Room 2  |\n|         ||         |\n");
        
        // Player marker for top
        if (currentRoom.getName().equals("Room 1")) {
            System.out.println("|  {+}    ||         |"); // Player in Room 1
        } else if (currentRoom.getName().equals("Room 2")) {
            System.out.println("|         ||  {+}    |"); // Player in Room 2
        } else {
            System.out.println("|         ||         |"); // Player not there
        }
        
        System.out.println("|                    |\n|         ||         |"); // Empty
        System.out.println("****  **********  ****"); // Middle wall
        System.out.println("   |  |        |  |   "); // Vertical walls
        System.out.println("****  **********  ****"); // Middle wall
        
        // Bottom (Rooms 4 and 3)
        System.out.print("| Room 4  || Room 3  |\n|         ||         |\n");
        
        // Player marker for bottom
        if (currentRoom.getName().equals("Room 4")) {
            System.out.println("|  {+}    ||         |"); // Player in Room 4
        } else if (currentRoom.getName().equals("Room 3")) {
            System.out.println("|         ||  {+}    |"); // Player in Room 3
        } else {
            System.out.println("|                    |"); // Player not there
        }
        
        System.out.println("|         ||         |\n|         ||         |"); // Empty
        System.out.println("**********************\n"); // Bottom wall
    }

    // Setup Game
    private static void setupGame() { // Initializes game world

        // Create all the Rooms
        Room room1 = new Room("Room 1"); // Create Room 1
        Room room2 = new Room("Room 2"); // Create Room 2
        Room room3 = new Room("Room 3"); // Create Room 3
        Room room4 = new Room("Room 4"); // Create Room 4
        Room room5 = new Room("Room 5"); // Create Room 5

        // Setup all the rooms individually

        // Setup Room 1 (top-left, north-west)
        String[] room1ExitDirections = {"east", "south"}; // Room 1 exits
        Room[] room1ExitDestinations = {room2, room4}; // Destination rooms for Room 1
        String[] room1Items = {"chair", "desk"}; // Items in Room 1
        String[] room1ItemDescriptions = { // Descriptions for Room 1 items
            "It is a chair",
            "It's a desk, there is a key on it."
        };
        String[] room1Grabbables = {"key"}; // Items you can take in Room 1
        room1.setExitDirections(room1ExitDirections); // Set exits
        room1.setExitDestinations(room1ExitDestinations); // Set exit destinations
        room1.setItems(room1Items); // Set visible items
        room1.setItemDescriptions(room1ItemDescriptions); // Set item descriptions
        room1.setGrabbables(room1Grabbables); // Set grabbable items

        // Setup Room 2 (top-right, north-east)
        String[] room2ExitDirections = {"west", "south"}; // Room 2 exits
        Room[] room2ExitDestinations = {room1, room3}; // Destination rooms for Room 2
        String[] room2Items = {"fireplace", "rug"}; // Items in Room 2
        String[] room2ItemDescriptions = { // Descriptions for Room 2 items
            "It's on fire",
            "There is a lump of coal on the rug."
        };
        String[] room2Grabbables = {"coal"}; // Items you can take in Room 2
        room2.setExitDirections(room2ExitDirections); // Set exits
        room2.setExitDestinations(room2ExitDestinations); // Set exit destinations
        room2.setItems(room2Items); // Set visible items
        room2.setItemDescriptions(room2ItemDescriptions); // Set item descriptions
        room2.setGrabbables(room2Grabbables); // Set grabbable items

        // Setup Room 3 (middle-right, south-east)
        String[] room3ExitDirections = {"north", "west"}; // Room 3 exits
        Room[] room3ExitDestinations = {room2, room4}; // Destination rooms for Room 3
        String[] room3Items = {"bookshelf", "painting"}; // Items in Room 3
        String[] room3ItemDescriptions = { // Descriptions for Room 3 items
            "The bookshelf contains an old tome",
            "The painting depicts a mysterious figure"
        };
        String[] room3Grabbables = {"tome"}; // Items you can take in Room 3
        room3.setExitDirections(room3ExitDirections); // Set exits
        room3.setExitDestinations(room3ExitDestinations); // Set exit destinations
        room3.setItems(room3Items); // Set visible items
        room3.setItemDescriptions(room3ItemDescriptions); // Set item descriptions
        room3.setGrabbables(room3Grabbables); // Set grabbable items

        // Setup Room 4 (middle-left, south-west)
        String[] room4ExitDirections = {"north", "east", "south"}; // Room 4 exits
        Room[] room4ExitDestinations = {room1, room3, room5}; // Destination rooms for Room 4
        String[] room4Items = {"chest", "mirror"}; // Items in Room 4
        String[] room4ItemDescriptions = { // Descriptions for Room 4 items
            "A sturdy wooden chest with an iron lock. It looks like it might contain something important.",
            "The mirror shows a reflection of another room"
        };
        String[] room4Grabbables = {"locket"}; // Items you can take in Room 4
        room4.setExitDirections(room4ExitDirections); // Set exits
        room4.setExitDestinations(room4ExitDestinations); // Set exit destinations
        room4.setItems(room4Items); // Set visible items
        room4.setItemDescriptions(room4ItemDescriptions); // Set item descriptions
        room4.setGrabbables(room4Grabbables); // Set grabbable items

    // Setup Room 5 (bottom-left)
    String[] room5ExitDirections = {"north"}; // Room 5 exits
    Room[] room5ExitDestinations = {room4}; // Destination rooms for Room 5
    String[] room5Items = {"cheese"}; // Items in Room 5
    String[] room5ItemDescriptions = { // Descriptions for Room 5 items
        "A single plate of cheese",
    };
    String[] room5Grabbables = {"cheese"}; // Items you can take in Room 5
    room4.setExitDirections(room5ExitDirections); // Set exits
    room4.setExitDestinations(room5ExitDestinations); // Set exit destinations
    room4.setItems(room5Items); // Set visible items
    room4.setItemDescriptions(room5ItemDescriptions); // Set item descriptions
    room4.setGrabbables(room5Grabbables); // Set grabbable items

        currentRoom = room1; // Start game in Room 1
    }
    
    @SuppressWarnings("java:S2189")
    public static void main(String[] args) { // Entry point of the program
        setupGame(); // Initialize rooms, items, and starting room

        while (true) { // Game loop, runs until program is terminated
            drawMap(); // Display map at start of each turn
            System.out.print(currentRoom.toString()); // Display current room description
            System.out.print("Inventory: "); // Prompt for inventory display

            for (int i = 0; i < inventory.length; i++) { // Loop through inventory slots
                System.out.print(inventory[i] + " "); // Print each inventory item
            }

            System.out.println("\nWhat would you like to do? "); // Prompt user for next action

            Scanner s = new Scanner(System.in); // Create Scanner to read input
            String input = s.nextLine(); // Read entire line of input
            String[] words = input.split(" "); // Split input into words

            if (words.length != 2) { // Check for proper two-word command
                status = DEFAULT_STATUS; // Set status to error message
                System.out.println(status); // Display error
                continue; // Skip to next loop iteration
            }

            String verb = words[0]; // First word is the action verb
            String noun = words[1]; // Second word is the target noun

            switch (verb) { // Decide which action to take
                case "go": // If verb is 'go'
                    handleGo(noun); // Move to another room
                    break;
                case "look": // If verb is 'look'
                    handleLook(noun); // Describe an item
                    break;
                case "take": // If verb is 'take'
                    handleTake(noun); // Pick up an item
                    break;
                case "open": // If verb is 'open'
                    handleOpen(noun); // Open an object
                    break;
                case "map": // Alternative way to view map
                    drawMap(); // Redraw map
                    status = ""; // Clear status
                    break;
                default: // If verb is unrecognized
                    status = DEFAULT_STATUS; // Set status to error message
            }

            System.out.println(status); // Print the status message
        }
    }
}

class Room { // Represents a game room
    private String name; // Room name
    private String[] exitDirections; // Directions you can go
    private Room[] exitDestinations; // Rooms reached by each direction
    private String[] items; // Items visible in the room
    private String[] itemDescriptions; // Descriptions for those items
    private String[] grabbables; // Items you can take

    public Room(String name) { // Constructor
        this.name = name; // Set the room's name
    }

    // Add getName() method
    public String getName() {
        return name;
    }

    public void setExitDirections(String[] exitDirections) { // Setter for exits
        this.exitDirections = exitDirections;
    }

    public String[] getExitDirections() { // Getter for exits
        return exitDirections;
    }

    public void setExitDestinations(Room[] exitDestinations) { // Setter for exit destinations
        this.exitDestinations = exitDestinations;
    }

    public Room[] getExitDestinations() { // Getter for exit destinations
        return exitDestinations;
    }

    public void setItems(String[] items) { // Setter for items
        this.items = items;
    }

    public String[] getItems() { // Getter for items
        return items;
    }

    public void setItemDescriptions(String[] itemDescriptions) { // Setter for descriptions
        this.itemDescriptions = itemDescriptions;
    }

    public String[] getItemDescriptions() { // Getter for descriptions
        return itemDescriptions;
    }

    public void setGrabbables(String[] grabbables) { // Setter for grabbable items
        this.grabbables = grabbables;
    }

    public String[] getGrabbables() { // Getter for grabbable items
        return grabbables;
    }

    @Override
    public String toString() { // Custom print for the room
        String result = "\nLocation: " + name; // Show room name
        result += "\nYou See: "; // List items
        for (String item : items) { // Loop items
            result += item + " "; // Append each item
        }
        result += "\nExits: "; // List exits
        for (String direction : exitDirections) { // Loop exits
            result += direction + " "; // Append each direction
        }
        return result + "\n"; // Return full description
    }
}