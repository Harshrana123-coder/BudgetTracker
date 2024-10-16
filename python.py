import tkinter as tk
from tkinter import ttk, messagebox
from datetime import datetime
from tkinter.scrolledtext import ScrolledText


class BudgetManagerTkinter(tk.Tk):
    def __init__(self):
        super().__init__()

        self.title("Budget Management System")
        self.geometry("600x400")

        self.incomes = []
        self.expenses = []

        # Panel for adding entries
        self.input_frame = ttk.Frame(self, padding="10")
        self.input_frame.grid(row=0, column=0, sticky="nsew")

        # Amount Input
        ttk.Label(self.input_frame, text="Amount:").grid(row=0, column=0, padx=10, pady=10, sticky="w")
        self.amount_var = tk.StringVar()
        self.amount_entry = ttk.Entry(self.input_frame, textvariable=self.amount_var)
        self.amount_entry.grid(row=0, column=1, padx=10, pady=10, sticky="ew")

        # Category Input
        ttk.Label(self.input_frame, text="Category:").grid(row=1, column=0, padx=10, pady=10, sticky="w")
        self.category_combo = ttk.Combobox(self.input_frame)
        self.category_combo.grid(row=1, column=1, padx=10, pady=10, sticky="ew")

        # Type Input (Income/Expense)
        ttk.Label(self.input_frame, text="Type:").grid(row=2, column=0, padx=10, pady=10, sticky="w")
        self.type_combo = ttk.Combobox(self.input_frame, values=["Income", "Expense"])
        self.type_combo.grid(row=2, column=1, padx=10, pady=10, sticky="ew")
        self.type_combo.bind("<<ComboboxSelected>>", self.update_categories)

        # Date Input
        ttk.Label(self.input_frame, text="Date:").grid(row=3, column=0, padx=10, pady=10, sticky="w")
        self.date_var = tk.StringVar(value=datetime.now().strftime("%Y-%m-%d"))
        self.date_entry = ttk.Entry(self.input_frame, textvariable=self.date_var)
        self.date_entry.grid(row=3, column=1, padx=10, pady=10, sticky="ew")

        # Add Entry Button
        self.add_button = ttk.Button(self.input_frame, text="Add Entry", command=self.add_entry)
        self.add_button.grid(row=4, column=0, columnspan=2, padx=10, pady=10, sticky="ew")

        # Show Report Button
        self.report_button = ttk.Button(self.input_frame, text="Show Report", command=self.show_report)
        self.report_button.grid(row=5, column=0, columnspan=2, padx=10, pady=10, sticky="ew")

        # Panel for showing tables
        self.table_frame = ttk.Frame(self)
        self.table_frame.grid(row=1, column=0, sticky="nsew")

        # Income and Expense Tables
        self.income_tree = ttk.Treeview(self.table_frame, columns=("Category", "Amount", "Date"), show="headings")
        self.income_tree.heading("Category", text="Category")
        self.income_tree.heading("Amount", text="Amount")
        self.income_tree.heading("Date", text="Date")
        self.income_tree.grid(row=0, column=0, padx=10, pady=10)

        self.expense_tree = ttk.Treeview(self.table_frame, columns=("Category", "Amount", "Date"), show="headings")
        self.expense_tree.heading("Category", text="Category")
        self.expense_tree.heading("Amount", text="Amount")
        self.expense_tree.heading("Date", text="Date")
        self.expense_tree.grid(row=0, column=1, padx=10, pady=10)

        self.grid_rowconfigure(1, weight=1)
        self.grid_columnconfigure(0, weight=1)

    def update_categories(self, event):
        if self.type_combo.get() == "Income":
            categories = ["Retail Sales", "Online Sales", "Consulting Fees", "Rental Income", "Investment Income"]
        else:
            categories = ["Food", "Transport", "Entertainment", "Bills", "Other"]
        self.category_combo.config(values=categories)
        self.category_combo.set('')

    def add_entry(self):
        try:
            amount = float(self.amount_var.get())
            category = self.category_combo.get()
            entry_type = self.type_combo.get()
            date = self.date_var.get()

            if entry_type == "Income":
                self.incomes.append({"category": category, "amount": amount, "date": date})
                self.income_tree.insert('', 'end', values=(category, amount, date))
            elif entry_type == "Expense":
                self.expenses.append({"category": category, "amount": amount, "date": date})
                self.expense_tree.insert('', 'end', values=(category, amount, date))

            messagebox.showinfo("Success", f"{entry_type} added successfully!")
        except ValueError:
            messagebox.showerror("Error", "Please enter a valid amount.")

    def show_report(self):
        report_window = tk.Toplevel(self)
        report_window.title("Budget Report")
        report_window.geometry("500x600")

        report_frame = ttk.Frame(report_window, padding="10")
        report_frame.pack(fill=tk.BOTH, expand=True)

        report_text = ScrolledText(report_frame, wrap=tk.WORD)
        report_text.pack(fill=tk.BOTH, expand=True)

        report_text.insert(tk.END, "Incomes:\n")
        for income in self.incomes:
            report_text.insert(tk.END, f"Income: {income['category']} - ${income['amount']} on {income['date']}\n")

        report_text.insert(tk.END, "\nExpenses:\n")
        for expense in self.expenses:
            report_text.insert(tk.END, f"Expense: {expense['category']} - ${expense['amount']} on {expense['date']}\n")


if __name__ == "__main__":
    app = BudgetManagerTkinter()
    app.mainloop()
