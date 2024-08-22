import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        start();
    }

    public static void start(){
        DataBaseUtil.createTable();

        boolean isQuit = false;
        while (!isQuit){
            showMenu();
            int action = getAction();
            switch (action){
                case 1:
                    addContact();
                    break;
                case 2:
                    getContactList();
                    break;
                case 3:
                    deleteContact();
                    break;
                case 4:
                    searchContact();
                    break;
                case 5:
                    editContact();
                    break;
                case 0:
                    isQuit = true;
                    break;
                default:
                    System.out.println("Invalid action");
                    break;
            }
        }
    }

    public static void showMenu(){
        System.out.println("***** Menu *****");
        System.out.println("1. Add Contact");
        System.out.println("2. Contact List");
        System.out.println("3. Delete Contact");
        System.out.println("4. Search Contact");
        System.out.println("5. Edit Contact");
        System.out.println("0. Exit");

    }

    public static int getAction(){
        System.out.println("Enter your choice: ");
        Scanner sc = new Scanner(System.in);
        int action = sc.nextInt();
        return action;
    }

    public static void addContact(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter contact name: ");
        String name = sc.next();
        System.out.println("Enter contact surname: ");
        String surname = sc.next();
        System.out.println("Enter contact phone number: ");
        String phone = sc.next();

        Contact contact = new Contact();
        contact.setName(name);
        contact.setSurname(surname);
        contact.setPhone(phone);

        // Check. Phone number already exist.
        ContactRepository contactRepository = new ContactRepository();
        boolean isPhoneNumAlreadyExist = contactRepository.checkPhoneNumber(phone);

        if (isPhoneNumAlreadyExist){
            System.out.println(phone + " already exists!");
            return;
        }

        // Save. Save contact.
        boolean isSavedContact = contactRepository.saveContact(contact);
        if (isSavedContact){
            System.out.println("Contact added");
        }else {
            System.out.println("Error occurred while saving contact!");
        }
    }

    public static void getContactList(){
        ContactRepository cr = new ContactRepository();
        List<Contact> contactList = cr.getAllContacts();
        if (contactList.isEmpty()){
            System.out.println("No contacts yet!");
        }

        for (int i=0; i< contactList.size(); i++) {
            Contact contact = contactList.get(i);
            System.out.println((i+1)+ ". " + contact.getName() + " " +
                    contact.getSurname() + " " + contact.getPhone());
        }
    }

    public static void deleteContact(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter phone number: ");
        String phone = sc.next();
        ContactRepository cr = new ContactRepository();

        int deletedRow = cr.deleteContact(phone);
        if (deletedRow != 0){
            System.out.println("Contact deleted!");
        }else{
            System.out.println("Contact not found!");
        }
    }

    public static void searchContact(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your query: ");
        String query = sc.next();
        ContactRepository cr = new ContactRepository();
        List<Contact> contactList = cr.searchContact(query);

        if (contactList.isEmpty()){
            System.out.println("No contacts found!");
        }else{
            for (int i=0; i< contactList.size(); i++) {
                Contact contact = contactList.get(i);
                System.out.println((i+1) + ". " + contact.getName() + " "
                + contact.getSurname() + " " + contact.getPhone());
            }
        }
    }

    public static void editContact(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter contact name: ");
        String name = sc.next();
        System.out.print("Enter contact surname: ");
        String surname = sc.next();
        System.out.print("Enter contact phone number: ");
        String phone = sc.next();
        Contact contact = new Contact();
        contact.setName(name);
        contact.setSurname(surname);
        contact.setPhone(phone);

        ContactRepository cr = new ContactRepository();
        if (cr.editContact(contact)){
            System.out.println("Contact updated!");
        }else {
            System.out.println("Contact not found!");
        }
    }
}