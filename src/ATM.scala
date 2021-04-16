import java.sql.{Connection, DriverManager, SQLException}
import java.util.Scanner

object ATM {
  /**
   * depositFunds: Make deposits to user accounts.
   * @param sc      For data capture.
   * @param db      For database queries.
   */
  def depositFunds(sc: Scanner, db : DatabaseManager) = {
    var amount : Float = 0
    var acctBal : Float = 0
    var memo : String = ""
    var toAcct : String = ""

    sc.nextLine()
    do{
      print("Enter the number id of the account you want to deposit to: ")
      toAcct = sc.nextLine()
      if(db.validateAccount(toAcct) == false)
        println("Invalid account. Please try again.")
    }while(db.validateAccount(toAcct) == false)

    acctBal = db.getAcctBalance(toAcct)

    do {
      print(f"Enter the amount (Balance: $acctBal%2.2f dlls): ")
      amount = sc.nextFloat()
      if (amount < 0)
        println("Amount must be greater than zero.")
    } while (amount <= 0)

    sc.nextLine()

    print("Enter a memo: ")
    memo = sc.nextLine()
    db.addAcctTransaction(toAcct, amount, memo)
    db.updateBalance(toAcct)

  }

  /**
   * withdrawFunds: Make withdrawals from user accounts.
   * @param sc      For data capture.
   * @param db      For database queries.
   */
  def withdrawFunds(sc : Scanner, db : DatabaseManager) = {
    var amount : Float = 0
    var acctBal : Float = 0
    var memo : String = ""
    var fromAcct : String = ""

    sc.nextLine()
    do{
      print("Enter the number id of the account you want to withdraw from: ")
      fromAcct = sc.nextLine()
      if(!db.validateAccount(fromAcct))
        println("Invalid account. Please try again.")
    }while(!db.validateAccount(fromAcct))

    acctBal = db.getAcctBalance(fromAcct)

    if(acctBal == 0) {
      println(f"No money available, balance = $acctBal%2.2f dlls")
    }
    else {
      do {
        print(f"Enter the amount (max: $acctBal%2.2f dlls): ")
        amount = sc.nextFloat()
        if (amount < 0)
          println("Amount must be greater than zero.")
        else if (amount > acctBal)
          println("Amount must not be greater than the actual balance.")
      } while (amount <= 0 || amount > acctBal)

      sc.nextLine()

      print("Enter a memo: ")
      memo = sc.nextLine()
      db.addAcctTransaction(fromAcct, -1 * amount, memo)
      db.updateBalance(fromAcct)
    }
  }

  /**
   * transferFunds: Make transfers between user accounts.
   * @param sc      For data capture.
   * @param db      For database queries.
   */
  def transferFunds(sc : Scanner, db : DatabaseManager) = {
    var fromAcct: String = ""
    var toAcct: String = ""
    var amount: Float = 0
    var acctBal: Float = 0

    sc.nextLine()
    do {
      print("Enter the id of the account to transfer from: ")
      fromAcct = sc.nextLine()
      if (!db.validateAccount(fromAcct))
        println("Invalid account. Please try again.")
    } while (!db.validateAccount(fromAcct))

    acctBal = db.getAcctBalance(fromAcct)

    if (acctBal == 0) {
      println(f"No money available, balance = $acctBal%2.2f")
    }
    else {
      do {
        print("Enter the id of the account to transfer to: ")
        toAcct = sc.nextLine()
        if (!db.validateAccount(toAcct))
          println("Invalid account. Please try again.")
      } while (!db.validateAccount(toAcct))

      do{
        print(f"Enter the amount to transfer (max: $acctBal%2.2f dlls): ")
        amount = sc.nextFloat()
        if (amount < 0)
          println("Amount must be greater than zero.")
        else if (amount > acctBal)
          println("Amount must not be greater than the actual balance.")
      }while(amount <= 0 || amount > acctBal)

      db.addAcctTransaction(fromAcct, -1 * amount, String.format("Transfer to account %s",toAcct))
      db.addAcctTransaction(toAcct, amount, String.format("Transfer from account %s", fromAcct))
      db.updateBalance(fromAcct)
      db.updateBalance(toAcct)
    }
  }

  /**
   * showTransHistory:  Shows the transaction history of an account.
   * @param sc      For data capture.
   * @param db      For database queries.
   */
  def showTransHistory(sc: Scanner, db : DatabaseManager) = {
    var acc : String = ""
    sc.nextLine()
    do{
      print("Enter the number id of the account whose transactions you want to see: ")
      acc = sc.nextLine()
      if(!db.validateAccount(acc))
        println("Invalid account. Please try again.")
    }while(!db.validateAccount(acc))

    db.printAcctTransHistory(acc)
  }

  /**
   * printUserMenu: Prints the options available to the user.
   * @param idUser  For the identification of the user with whom the queries will be made.
   * @param sc      For data capture.
   * @param db      For database queries.
   */
  def printUserMenu(idUser: String, sc: Scanner, db: DatabaseManager) = {
    var opc : Int = 0
    do{
      println("Welcome " + db.getUserName(idUser) + ", what would you like to do?")
      println("[1] Show accounts summary")
      println("[2] Show account transaction history")
      println("[3] Withdraw")
      println("[4] Deposit")
      println("[5] Transfer")
      println("[6] Quit")
      println()
      print("Enter choice: ")
      opc = sc.nextInt()
      opc match {
        case 1 => db.printAccountsSummary(idUser)
        case 2 => ATM.showTransHistory(sc, db)
        case 3 => ATM.withdrawFunds(sc, db)
        case 4 => ATM.depositFunds(sc, db)
        case 5 => ATM.transferFunds(sc, db)
        case 6 => println("Good bye!")
      }
      println()
    }while(opc != 6)
  }

  /**
   * mainMenuPrompt: Print and capture data for log in.
   * @param sc       For data capture.
   * @param db       For database queries.
   */
  def mainMenuPrompt(sc: Scanner, db :DatabaseManager) = {
    var userId : String = ""
    var userPin : String = ""
    val nameBank = db.getBankName(1)

    do{
      print("\nWelcome to " + nameBank + "\n")
      print("Enter user ID: ")
      userId = sc.nextLine()
      print("Enter pin: ")
      userPin = sc.nextLine()
      if(!db.login(userId, userPin))
        println("Incorrect user ID/pin combination. Please try again.")
    }while(!db.login(userId, userPin))

    println()
    printUserMenu(userId, sc, db)

  }

  //main method
  def main(args: Array[String]): Unit = {
    val sc : Scanner = new Scanner(System.in)
    val db : DatabaseManager = new DatabaseManager("jdbc:mysql://localhost/atm",
      "root", "root")

    while(true){
      ATM.mainMenuPrompt(sc, db)
    }
  }
}