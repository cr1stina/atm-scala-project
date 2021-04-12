import java.util.ArrayList
/**
 * Class Account:
 * @param name    the name to identify the account.
 * @param holder  user the account is assigned to.
 * @param bank    bank to which the account belongs.
 */
class Account(var name : String, var holder : User, var bank: Bank) {
  /**
   * id: unique number for the identification of the bank account.
   * transaction: the list of transactions for this account.
   */
  private var id : String = null
  private var transactions : ArrayList[Transaction] = _

  //initialize transaction list and id
  id = bank.getNewAccountID()
  transactions = new ArrayList[Transaction]()
  //set the account to the user and user
  holder.addAccount(this)
  bank.addAccount(this)

  //returns the account id
  def get_id(): String = {
    id
  }

  //returns the account summary
  def getSummary(): String = {
    val balance : Double = this.get_balance()
    if(balance >= 0){
      String.format("%s : $%.02f : %s", this.id, balance, this.name)
    }
    else{
      String.format("%s : $(%.02f) : %s", this.id, balance, this.name)
    }
  }

  //calculates and returns the current account balance
  def get_balance(): Double = {
    var balance : Double = 0
    var transaction : Transaction = null
    for(i <- 0 until this.transactions.size()){
      transaction = this.transactions.get(i)
      balance += transaction.get_amount()
    }
    balance
  }

  //prints account transaction history
  def printTransHistory() = {
    print("\nTransaction history for account " + this.id + "\n")
    if(this.transactions.isEmpty){
      println("No transactions to show.")
    }else {
      for(i <- 0 until this.transactions.size()){
        println(this.transactions.get(i).getHistory())
      }
    }
    println()
  }

  //add a new account transaction to the list
  def addTransaction(amount: Double, memo: String) = {
    val transaction : Transaction = new Transaction(amount, this, memo)
    this.transactions.add(transaction)
  }

};
