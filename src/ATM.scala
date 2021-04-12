import java.util.Scanner

object ATM {
  /**
   * depositFunds: make a deposit to an account.
   * @param currentUser user with whom the deposit is made.
   * @param sc          to read the data entered by the user.
   */
  def depositFunds(currentUser: User, sc: Scanner) = {
    var toAcct : Int = 0
    var amount : Double = 0
    var acctBal : Double = 0
    var memo : String = ""

    do{
      print(s"Enter the number (1 - ${currentUser.numAccounts()}) of the account to deposit from: ")
      toAcct = sc.nextInt() - 1
      if(toAcct < 0 || toAcct >= currentUser.numAccounts())
        println("Invalid account. Please try again.")
    }while(toAcct < 0 || toAcct >= currentUser.numAccounts())

    acctBal = currentUser.getAcctBalance(toAcct)

    do{
      print(f"Enter the amount (Balance: $acctBal%2.2f dlls): ")
      amount = sc.nextDouble()
      if(amount < 0)
        println("Amount must be greater than zero.")
    }while(amount <= 0)

    sc.nextLine()

    print("Enter a memo: ")
    memo = sc.nextLine()
    currentUser.addAcctTransaction(toAcct, amount, memo)
    println()
  }

  /**
   * withdrawFunds: make a withdraw from an account.
   * @param currentUser user with whom the withdraw is made.
   * @param sc          to read the data entered by the user.
   */
  def withdrawFunds(currentUser: User, sc: Scanner) = {
    var fromAcct : Int = 0
    var amount : Double = 0
    var acctBal : Double = 0
    var memo : String = ""

    do{
      print(s"Enter the number (1 - ${currentUser.numAccounts()}) of the account to transfer from: ")
      fromAcct = sc.nextInt() - 1
      if(fromAcct < 0 || fromAcct >= currentUser.numAccounts())
        println("Invalid account. Please try again.")
    }while(fromAcct < 0 || fromAcct >= currentUser.numAccounts())

    acctBal = currentUser.getAcctBalance(fromAcct)
    if(acctBal == 0) {
      println(f"No money available, balance = $acctBal%2.2f dlls")
      println()
    }
    else {

      do {
        print(f"Enter the amount (max: $acctBal%2.2f dlls): ")
        amount = sc.nextDouble()
        if (amount < 0)
          println("Amount must be greater than zero.")
        else if (amount > acctBal)
          println("Amount must not be greater than the actual balance.")
      } while (amount <= 0 || amount > acctBal)

      sc.nextLine()

      print("Enter a memo: ")
      memo = sc.nextLine()
      currentUser.addAcctTransaction(fromAcct, -1 * amount, memo)
    }
    println()

  }

  /**
   * transferFunds: make a transfer from an account.
   * @param currentUser user with whom the transfer is made.
   * @param sc          to read the data entered by the user.
   */
  def transferFunds(currentUser: User, sc: Scanner) = {
    var fromAcct : Int = 0
    var toAcct : Int = 0
    var amount : Double = 0
    var acctBal : Double = 0

    do{
      print(s"Enter the number (1 - ${currentUser.numAccounts()}) of the account to transfer from: ")
      fromAcct = sc.nextInt() - 1
      if(fromAcct < 0 || fromAcct >= currentUser.numAccounts())
        println("Invalid account. Please try again.")
    }while(fromAcct < 0 || fromAcct >= currentUser.numAccounts())

    acctBal = currentUser.getAcctBalance(fromAcct)
    if(acctBal == 0) {
      println(f"No money available, balance = $acctBal%2.2f")
    }
    else {
      do {
        print(s"Enter the number (1 - ${currentUser.numAccounts()}) of the account to transfer: ")
        toAcct = sc.nextInt() - 1
        if (toAcct < 0 || toAcct >= currentUser.numAccounts())
          println("Invalid account. Please try again.")
      } while (toAcct < 0 || toAcct >= currentUser.numAccounts())

      do {
        print(f"Enter the amount to transfer (max: $acctBal%2.2f dlls): ")
        amount = sc.nextDouble()
        if (amount < 0)
          println("Amount must be greater than zero.")
        else if (amount > acctBal)
          println("Amount must not be greater than the actual balance.")
      } while (amount <= 0 || amount > acctBal)

      currentUser.addAcctTransaction(fromAcct, -1 * amount,
        String.format("Transfer to account %s", currentUser.getAcctId(toAcct)))
      currentUser.addAcctTransaction(toAcct, amount,
        String.format("Transfer from account %s", currentUser.getAcctId(fromAcct)))
    }
    println()
  }

  /**
   * showTransHistory: prints the history of a selected account.
   * @param currentUser user requesting the information.
   * @param sc          to read the data entered by the user.
   */
  def showTransHistory(currentUser: User, sc: Scanner) = {
    var acc : Int = 0
    do{
      print("Enter the number (1 - " + currentUser.numAccounts() + ") of the account " +
        "whose transactions you want to see: ")
      acc = sc.nextInt() - 1
      if(acc < 0 || acc >= currentUser.numAccounts())
        println("Invalid account. Please try again.")
    }while(acc < 0 || acc >= currentUser.numAccounts())

    currentUser.printAcctTransHistory(acc)
  }

  /**
   * printUserMenu: prints the user menu
   * @param currentUser current user
   * @param sc          to read the data entered by the user.
   */
  def printUserMenu(currentUser: User, sc: Scanner) = {
    var opc : Int = 0
    do{
      println("Welcome " + currentUser.get_name() + " ,what would you like to do?")
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
        case 1 => currentUser.printAccountsSummary()
        case 2 => ATM.showTransHistory(currentUser, sc)
        case 3 => ATM.withdrawFunds(currentUser, sc)
        case 4 => ATM.depositFunds(currentUser, sc)
        case 5 => ATM.transferFunds(currentUser, sc)
        case 6 => println("Good bye!")
      }
    }while(opc != 6)
  }

  /**
   * mainMenuPrompt: ATM main menu
   * @param bank  current bank.
   * @param sc    to read the data entered by the user.
   */
  def mainMenuPrompt(bank: Bank, sc: Scanner): User = {
    var userId : String = ""
    var userPin : String = ""
    var authUser : User = null

    do{
        print("\nWelcome to " + bank.get_name() + "\n")
        print("Enter user ID: ")
        userId = sc.nextLine()
        print("Enter pin: ")
        userPin = sc.nextLine()
        authUser = bank.userLogin(userId, userPin)
        if(authUser == null)
          println("Incorrect user ID/pin combination. Please try again.")
    }while(authUser == null)

    authUser
  }

  //main method
  def main(args: Array[String]): Unit = {
      val sc : Scanner = new Scanner(System.in)
      val bank : Bank = new Bank("Bank of Tijuana")
      val user : User = bank.newUser("Cristina", "Cazares", "1234")
      val acc : Account = new Account("Checking", user, bank)
      bank.addAccount(acc)

      var currentUser : User = null
      while(true){
        currentUser = ATM.mainMenuPrompt(bank, sc)
        ATM.printUserMenu(currentUser, sc)
      }

  }
}
