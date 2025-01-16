import os
import shutil

def move_captures():
    # Source directory (where Sikuli saves captures)
    sikuli_dir = os.path.expanduser("~/Desktop") # Sikuli typically saves to Desktop
    
    # Target directory
    target_dir = "/Users/oluwabamise/flutter-e2e-tests/src/test/resources/images/onboarding"
    
    # Create target directory if it doesn't exist
    os.makedirs(target_dir, exist_ok=True)
    
    # Files to move
    files_to_move = {
        "welcome-text.png": None,
        "get-started-button.png": None
    }
    
    # Find the most recent captures
    for file in os.listdir(sikuli_dir):
        if file.endswith(".png"):
            file_path = os.path.join(sikuli_dir, file)
            # Get file creation time
            ctime = os.path.getctime(file_path)
            
            # Check if it might be one of our captures
            for target_file in files_to_move:
                if files_to_move[target_file] is None or ctime > os.path.getctime(files_to_move[target_file]):
                    files_to_move[target_file] = file_path
    
    # Move the files
    for target_name, source_path in files_to_move.items():
        if source_path:
            target_path = os.path.join(target_dir, target_name)
            shutil.copy2(source_path, target_path)
            print(f"Moved {source_path} to {target_path}")

if __name__ == "__main__":
    move_captures()
