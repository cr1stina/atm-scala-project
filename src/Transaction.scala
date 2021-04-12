import java.util.Date
/**
 * Class Transaction:
 * @param amount  transaction amount.
 * @param account account in which the transaction was made.
 * @param memo    identification message for the movement made.
 */
class Transaction(var amount : Double, var account: Account, var memo : String){
  /**
   * timeStamp: date the movement was made.
   */
  private var timeStamp : Date = _
  //initialize the date
  this.timeStamp = new Date()

  //returns the amount of the transaction
  def get_amount() : Double = {
    amount
  }

  //returns the information of a transaction
  def getHistory(): String = {
    if(amount >= 0){
      String.format("%s : $%.02f : %s", this.timeStamp.toString(), amount, memo)
    }
    else{
      String.format("%s : $(%.02f) : %s", this.timeStamp.toString(), amount, memo)
    }
  }


};
