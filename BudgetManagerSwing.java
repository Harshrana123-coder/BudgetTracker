import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BudgetManagerSwing extends JFrame {

    private List<Entry> incomes = new ArrayList<>();
    private List<Entry> expenses = new ArrayList<>();
    
    private DefaultTableModel incomeTableModel;
    private DefaultTableModel expenseTableModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BudgetManagerSwing frame = new BudgetManagerSwing();
            frame.setVisible(true);
        });
    }

    public BudgetManagerSwing() {
        setTitle("Budget Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 400);

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(2, 1, 10, 10));

        // Panel for adding entries
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountInput = new JTextField();
        inputPanel.add(amountLabel);
        inputPanel.add(amountInput);

        JLabel categoryLabel = new JLabel("Category:");
        JComboBox<String> categoryInput = new JComboBox<>();
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryInput);

        JLabel typeLabel = new JLabel("Type:");
        JComboBox<String> typeInput = new JComboBox<>(new String[]{"Income", "Expense"});
        inputPanel.add(typeLabel);
        inputPanel.add(typeInput);

        JLabel dateLabel = new JLabel("Date:");
        JTextField dateInput = new JTextField(LocalDate.now().toString());
        inputPanel.add(dateLabel);
        inputPanel.add(dateInput);

        JButton addButton = new JButton("Add Entry");
        inputPanel.add(addButton);

        JButton reportButton = new JButton("Show Report");
        inputPanel.add(reportButton);

        contentPane.add(inputPanel);

        // Table for showing data
        JPanel tablePanel = new JPanel(new BorderLayout());
        incomeTableModel = new DefaultTableModel(new String[]{"Category", "Amount", "Date"}, 0);
        expenseTableModel = new DefaultTableModel(new String[]{"Category", "Amount", "Date"}, 0);

        JTable incomeTable = new JTable(incomeTableModel);
        JTable expenseTable = new JTable(expenseTableModel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(incomeTable), new JScrollPane(expenseTable));
        splitPane.setDividerLocation(300);

        tablePanel.add(new JLabel("Incomes (Left) | Expenses (Right)"), BorderLayout.NORTH);
        tablePanel.add(splitPane, BorderLayout.CENTER);

        contentPane.add(tablePanel);

        // Event Handlers
        typeInput.addActionListener(e -> {
            if (typeInput.getSelectedItem().equals("Income")) {
                categoryInput.setModel(new DefaultComboBoxModel<>(new String[]{
                        "Retail Sales", "Online Sales", "Consulting Fees", "Rental Income", "Investment Income"
                }));
            } else if (typeInput.getSelectedItem().equals("Expense")) {
                categoryInput.setModel(new DefaultComboBoxModel<>(new String[]{
                        "Food", "Transport", "Entertainment", "Bills", "Other"
                }));
            }
        });

        addButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountInput.getText());
                String category = (String) categoryInput.getSelectedItem();
                String type = (String) typeInput.getSelectedItem();
                LocalDate date = LocalDate.parse(dateInput.getText(), DateTimeFormatter.ISO_DATE);

                if ("Income".equals(type)) {
                    incomes.add(new Entry(amount, category, date));
                    incomeTableModel.addRow(new Object[]{category, amount, date});
                } else if ("Expense".equals(type)) {
                    expenses.add(new Entry(amount, category, date));
                    expenseTableModel.addRow(new Object[]{category, amount, date});
                }

                JOptionPane.showMessageDialog(this, type + " added successfully!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showReport();
            }
        });
    }

    private void showReport() {
        JFrame reportFrame = new JFrame("Budget Report");
        reportFrame.setBounds(100, 100, 500, 600);
        reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));

        reportPanel.add(new JLabel("Incomes:"));
        for (Entry income : incomes) {
            reportPanel.add(new JLabel("Income: " + income.getCategory() + " - $" + income.getAmount() + " on " + income.getDate()));
        }

        reportPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        reportPanel.add(new JLabel("Expenses:"));
        for (Entry expense : expenses) {
            reportPanel.add(new JLabel("Expense: " + expense.getCategory() + " - $" + expense.getAmount() + " on " + expense.getDate()));
        }

        JScrollPane scrollPane = new JScrollPane(reportPanel);
        reportFrame.getContentPane().add(scrollPane);
        reportFrame.setVisible(true);
    }

    // Entry class to store data
    class Entry {
        private double amount;
        private String category;
        private LocalDate date;

        public Entry(double amount, String category, LocalDate date) {
            this.amount = amount;
            this.category = category;
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }

        public String getCategory() {
            return category;
        }

        public LocalDate getDate() {
            return date;
        }
    }
}
