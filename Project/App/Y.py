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
        icon = tk.PhotoImage(file=os.path.join(self.current_path, "Y/App/Y_icon.png"))  # Replace with your icon file path
        self.iconphoto(False, icon)

        self.init_login_screen()

    def connect(self):
        self.client_socket.connect((self.host, self.port))

    def init_login_screen(self):
        self.login_frame = tk.Frame(self, bg="#e6eaec")
        self.login_frame.pack(fill="both", expand=True)

        container = tk.Frame(self.login_frame, bg="#e6eaec")
        container.place(relx=0.5, rely=0.5, anchor="center")

        # Load the image with PIL and resize it
        pil_image = Image.open(os.path.join(self.current_path, "Y/App/Y_icon.png")) # Replace with your image path
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
            message = {
                "type": "LOGIN",  # Adding the identifier
                "username": self.username,
                "password": password
            }
            message_string = json.dumps(message)
            self.client_socket.sendall(message_string+"\n".encode('utf-8'))
            response = self.client_socket.recv(1000)
            if response == "NULL_USER":
                self.error_label.config(text="Invalid username or password.")
                return
            elif response == "OK":
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
        ttk.Button(self.functions_frame, text="Home").pack(pady=(10,10))
        ttk.Button(self.functions_frame, text="Refresh", command = self.refresh_feed).pack(pady=(10,10))
        ttk.Button(self.functions_frame, text="Profile").pack(pady=(10,10))
        ttk.Button(self.functions_frame, text="Log Out", command = self.logout).pack(side="bottom")

        # Delay populating the feed to ensure layout is updated
        self.after(50, self.load_feed)
    
    def perform_search(self):
        search_query = self.search_var.get()
        # Placeholder for server communication
        if search_query!="":
            #response = self.send_search_query_to_server(search_query)
            response="null_user"
            if response == "null_user":
                # Display error in reversed text
                messagebox.showerror("Error", "No results found for: " +search_query)
            else:
                self.open_user()

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

    def load_feed(self):
        # Update the UI and calculate wraplength based on the width of the scrollable_frame
        self.update_idletasks()
        wrap_length = self.width*0.60
        # Sample Titles and Contents
        sample_titles = ["usuario_1", "usuario_2", "usuario_3", "usuario_4", "usuario_5"]
        sample_contents = ["Dissuade ecstatic and properly saw entirely sir why laughter endeavor. In on my jointure horrible margaret suitable he followed speedily. Indeed vanity excuse or mr lovers of on. By offer scale an stuff. Blush be sorry no sight. Sang lose of hour then he left find. \n Talking chamber as shewing an it minutes. Trees fully of blind do. Exquisite favourite at do extensive listening. Improve up musical welcome he. Gay attended vicinity prepared now diverted. Esteems it ye sending reached as. Longer lively her design settle tastes advice mrs off who.","Maids table how learn drift but purse stand yet set. Music me house could among oh as their. Piqued our sister shy nature almost his wicket. Hand dear so we hour to. He we be hastily offence effects he service. Sympathize it projection ye insipidity celebrated my pianoforte indulgence. Point his truth put style. Elegance exercise as laughing proposal mistaken if. We up precaution an it solicitude acceptance invitation.","We diminution preference thoroughly if. Joy deal pain view much her..","Nice post","Good morning"]

        for i in range(len(sample_titles)*10):
            post_title = sample_titles[i%5]
            post_content = sample_contents[i%5]

            post_frame = tk.Frame(self.feed_frame,width=self.width*0.85, bg="white", bd=0, relief="flat", highlightbackground="#e6eaec", highlightcolor="#e6eaec", highlightthickness=1)
            #username
            tk.Label(post_frame, text=post_title, bg="white", fg="black", font=("Calibri", 14, "bold"), wraplength=wrap_length, anchor='w', justify='left').pack(padx=10, pady=10, fill='x')
            #Content
            tk.Label(post_frame, text=post_content, bg="white", fg="black", font=("Calibri", 12), wraplength=wrap_length, anchor='w', justify='left').pack(padx=10, pady=5, fill='x')
            #Spacer
            tk.Label(post_frame, text="", bg="white", fg="black", font=("Calibri", 14, "bold"), wraplength=wrap_length, anchor='w', justify='left').pack(padx=0, pady=0, fill='x')

            post_frame.pack(fill=tk.X, padx=10, pady=0,anchor="center")
        self.after(5, lambda: self.canvas.yview_moveto(0))

    def on_mousewheel(self, event, canvas):
        scroll_speed_windows = 1
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
        self.username=""
        #Return to login Page
        self.after(100, lambda: self.init_login_screen())
    
    def terminate(self): 
        self.client_socket.sendall("CLOSE".encode())
        self.client_socket.close()
        self.destroy()
        
if __name__ == "__main__":
    app = Application("localhost", 5555)
    app.connect()
    app.mainloop()
    app.terminate()
