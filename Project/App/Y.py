import json
import os
import socket
import tkinter as tk
from tkinter import ttk, PhotoImage
from tkinter import messagebox
from PIL import Image, ImageTk
import platform
   
class Application(tk.Tk):
    def __init__(self, host, port):
        super().__init__()
        self.client_socket = socket.socket()
        self.host = host
        self.port = port
        self.title("Y")
        self.center_window()
        self.configure(bg="#e6eaec")
        # Load the icon
        self.current_path = os.getcwd()
        icon = tk.PhotoImage(file=os.path.join(self.current_path, "Y_icon.png"))  # Replace with your icon file path
        self.iconphoto(False, icon)

        self.init_login_screen()

    def connect(self):
        self.client_socket.connect((self.host, self.port))
    
    def send_request(self, request_type, args):
        message = {
            "type": request_type
        }
        message.update(args)
        message_string = json.dumps(message) + "\n"
        self.client_socket.sendall(message_string.encode('utf-8'))
        response = self.client_socket.recv(1000000000).decode('utf-8')
        print(response)
        return response

    def init_login_screen(self):
        self.login_frame = tk.Frame(self, bg="#e6eaec")
        self.login_frame.pack(fill="both", expand=True)

        container = tk.Frame(self.login_frame, bg="#e6eaec")
        container.place(relx=0.5, rely=0.5, anchor="center")

        # Load the image with PIL and resize it
        pil_image = Image.open(os.path.join(self.current_path, "Y_icon.png")) # Replace with your image path
        pil_image = pil_image.resize((180, 170), Image.LANCZOS)  # Resize to 150x150 or your desired size

        self.login_image = ImageTk.PhotoImage(pil_image)
        image_label = tk.Label(container, image=self.login_image, bg="#e6eaec")
        image_label.pack(pady=10)

        tk.Label(container, text="Welcome to Y \n Please Login to continue", font=("Calibri", 21), bg="#e6eaec").pack(pady=10)

        # Username field
        tk.Label(container, text="Username", font=("Calibri", 14), bg="#e6eaec").pack(pady=(10, 0))
        self.username_entry = tk.Entry(container, font=("Calibri", 12), highlightthickness=1, borderwidth=1, relief="flat")
        self.username_entry.pack(pady=(0, 10))

        # Password field
        tk.Label(container, text="Password", font=("Calibri", 14), bg="#e6eaec").pack(pady=(10, 0))
        self.password_entry = tk.Entry(container, show="*", font=("Calibri", 12), highlightthickness=1, borderwidth=1, relief="flat")
        self.password_entry.pack(pady=(0, 10))

        # Error message label
        self.error_label = tk.Label(container, text="", font=("Calibri", 12), bg="#e6eaec", fg="red")
        self.error_label.pack()

        ttk.Button(container, text="Enter", command=self.authenticate).pack(pady=0)

    def authenticate(self):
        self.username = self.username_entry.get()
        password = self.password_entry.get()

        # Basic error handling
        if not self.username or not password:
            self.error_label.config(text="Please fill in all fields.")
            return
        else:
            response = self.send_request("LOGIN", {"username": self.username, "password": password})
            if response == "NULL_LOGIN\n":
                self.error_label.config(text="Invalid username or password.")
            elif response == "OK\n":
                self.error_label.config(text="")
                self.login_frame.destroy()
                self.create_widgets()
            else: return

    def center_window(self):
        screen_width = self.winfo_screenwidth()
        screen_height = self.winfo_screenheight()
        self.width = int(screen_width * 0.7)
        height = int(screen_height * 0.85)
        x = int((screen_width / 2) - (self.width / 2))
        y = int((screen_height / 2) - (height / 2))
        self.geometry(f'{self.width}x{height}+{x}+{y}')
        self.resizable(0, 1)

    def create_widgets(self):
        # Create and pack the functions_frame
        functions_frame_width = int(self.width * 0.20)
        self.functions_frame = tk.Frame(self, bg="#e6eaec", width=functions_frame_width)
        self.functions_frame.pack(side="left", fill="y", pady=20, padx=10)

        # Scrollable feed list and title label
        self.scrollable_frame = tk.Frame(self, bg="#e6eaec", bd=10)
        self.scrollable_frame.pack(side="top", fill="both", expand=True)
        
        # Title and Search Box Container
        top_frame = tk.Frame(self.scrollable_frame, bg="#e6eaec")
        top_frame.pack(fill=tk.X, padx=10, pady=10)
        
        title = tk.Label(top_frame, text="Welcome back", font=("Calibri", 20), bg="#e6eaec", fg="black", anchor="w")
        title.pack(side=tk.LEFT)
        user_text = self.truncate_text(self.username, 20)  # Limit to 30 characters
        usr = tk.Label(top_frame, text=user_text, font=("Calibri", 20, "bold"), bg="#e6eaec", fg="black", anchor="w")
        usr.pack(side=tk.LEFT)
        
        # Search Box
        search_button = ttk.Button(top_frame, text="🔎", width=2,command=self.perform_search)
        search_button.pack(side=tk.RIGHT)
        self.search_var = tk.StringVar()
        search_box = tk.Entry(top_frame, textvariable=self.search_var, highlightthickness=1,bd=1,font=("Calibri", 12), borderwidth=0, relief="groove")
        search_box.pack(side=tk.RIGHT, padx=(2,10))
        # Search Label
        search_label = tk.Label(top_frame, text="Search:", font=("Calibri", 14), bg="#e6eaec", fg="black")
        search_label.pack(side=tk.RIGHT, padx=(0))
        self.create_feed_frame()

        # Create a frame for search results
        self.search_results_frame = tk.Frame(self.scrollable_frame, bg="#e6eaec")
        # Initially hidden
        self.search_results_frame.pack_forget()

        # Add buttons to the functions_frame
        ttk.Button(self.functions_frame, text="Home",command=self.return_home).pack(pady=(10,10))
        ttk.Button(self.functions_frame, text="Refresh", command = self.refresh_feed).pack(pady=(10,10))
        ttk.Button(self.functions_frame, text="New Post").pack(pady=(10,10))
        ttk.Button(self.functions_frame, text="Log Out", command = self.logout).pack(side="bottom")
        ttk.Button(self.functions_frame, text="Profile").pack(side="bottom",pady=(10,20))

        # Delay populating the feed to ensure layout is updated
        self.after(50, self.load_feed)
    
    def perform_search(self):
        search_query = self.search_var.get()
        # Placeholder for server communication
        if search_query!="":
            #response = self.send_search_query_to_server(search_query)
            response=self.send_request("GET_USER", {"username": search_query})
            if response == "NULL_USER\n":
                # Display error
                messagebox.showerror("Error", "No results found for: " +search_query)
            else:
                self.open_user(search_query,response)
    def open_profile(self):
        response=self.send_request("GET_USER", {"username": self.username})
        self.open_user(self.username,response)
        
    def open_user(self, username, response):
        # Close existing overlay if it's open
        self.close_user_overlay()

        # Create an overlay frame
        self.user_overlay = tk.Frame(self, bg="lightgray", width=self.width, height=self.winfo_height())
        self.user_overlay.place(relx=0.5, rely=0.5, anchor="center")

        # Add a label to show the username
        tk.Label(self.user_overlay, text=username, font=("Calibri", 16)).pack(pady=10)

        # Iterate over the response and create labels for each post
        for post in response:
            post_frame = tk.Frame(self.user_overlay, bg="white", bd=0, relief="flat")
            tk.Label(post_frame, text=post['username'], font=("Calibri", 14, "bold")).pack(padx=10, pady=10, fill='x')
            tk.Label(post_frame, text=post['content'], font=("Calibri", 12)).pack(padx=10, pady=(5,20), fill='x')
            post_frame.pack(fill=tk.X, padx=10, pady=0)

        # Add a close button to the overlay
        close_button = ttk.Button(self.user_overlay, text="Close", command=self.close_user_overlay)
        close_button.pack(pady=10)

    def close_user_overlay(self):
        if hasattr(self, 'user_overlay'):
            self.user_overlay.destroy()



    def create_feed_frame(self):
        # Create a canvas and a scrollbar
        self.canvas = tk.Canvas(self.scrollable_frame, bg="#e6eaec",highlightthickness=0, borderwidth=0)
        self.scrollbar = ttk.Scrollbar(self.scrollable_frame, orient="vertical", command=self.canvas.yview)
        self.canvas.configure(yscrollcommand=self.scrollbar.set)
        self.canvas.pack(side="left", fill="both", expand=True, anchor="center")
        self.scrollbar.pack(side="right", fill="y")

        # Create feed_frame inside the canvas
        self.feed_frame = tk.Frame(self.canvas, bg="#e6eaec", highlightthickness=0)
        self.canvas.create_window((0, 0), window=self.feed_frame, anchor="center")

        def on_frame_configure(event):
            self.canvas.configure(scrollregion=self.canvas.bbox("all"))

        self.feed_frame.bind("<Configure>", on_frame_configure)
        self.bind_all("<MouseWheel>", lambda event: self.on_mousewheel(event, self.canvas))
    
    def truncate_text(self, text, max_length):
        """ Truncate text to a maximum length, appending ellipsis if truncated. """
        return text if len(text) <= max_length else text[:max_length-3] + "..."

    def load_user(self,username):
        self.user_frame

    def return_home(self):
        if hasattr(self, 'user_frame'):
            self.user_frame.destroy()

    def load_feed(self):
        # Update the UI and calculate wraplength based on the width of the scrollable_frame
        self.update_idletasks()
        self.wrap_length = self.width*0.60
        # Sample Titles and Contents
        feed_json = self.send_request("FEED", {"username": self.username})
        print(feed_json)
        feed = json.loads(feed_json)  # Convert JSON string to Python list

        for post in feed:
            post_frame = tk.Frame(self.feed_frame, width=self.width*0.85, bg="white", bd=0, relief="flat", highlightbackground="#e6eaec", highlightcolor="#e6eaec", highlightthickness=1)
            # Username
            tk.Label(post_frame, text=post['username'], bg="white", fg="black", font=("Calibri", 14, "bold"), wraplength=self.wrap_length, anchor='w', justify='left').pack(padx=10, pady=10, fill='x')
            # Content
            tk.Label(post_frame, text=post['content'], bg="white", fg="black", font=("Calibri", 12), wraplength=self.wrap_length, anchor='w', justify='left').pack(padx=10, pady=(5,20), fill='x')

            post_frame.pack(fill=tk.X, padx=10, pady=0, anchor="center")

            self.after(5, lambda: self.canvas.yview_moveto(0))

    def on_mousewheel(self, event, canvas):
        scroll_speed_windows = 2
        scroll_speed_other = 3

        if platform.system() == "Windows":
            delta = int(-1*(event.delta/120)) * scroll_speed_windows
        else:
            delta = int(-1*event.delta) * scroll_speed_other
        canvas.yview_scroll(delta, "units")
    
    def refresh_feed(self):
        # Destroy existing feed frame and recreate it
        self.canvas.destroy()
        self.feed_frame.destroy()
        self.scrollbar.destroy()
        self.create_feed_frame()
        self.load_feed()
   
    def logout(self):
        # Destroy the main interface frames/widgets if they exist
        if hasattr(self, 'scrollable_frame'):
            self.scrollable_frame.destroy()
        if hasattr(self, 'feed_frame'):
            self.feed_frame.destroy()
        if hasattr(self, 'canvas'):
            self.canvas.destroy()
        if hasattr(self, 'scrollbar'):
            self.scrollbar.destroy()
        if hasattr(self, 'functions_frame'):
            self.functions_frame.destroy()
        if hasattr(self, 'top_frame'):
            self.top_frame.destroy()
        if hasattr(self, 'search_results_frame'):
            self.search_results_frame.destroy()
        self.send_request("LOGOUT", {"username": self.username})
        self.username=""
        #Return to login Page
        self.after(100, lambda: self.init_login_screen())
    
    def terminate(self): 
        self.send_request("CLOSE","")
        self.client_socket.close()
        self.destroy()
        
if __name__ == "__main__":
    app = Application("localhost", 5555)
    app.connect()
    app.mainloop()
    app.terminate()
