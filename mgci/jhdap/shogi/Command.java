package mgci.jhdap.shogi;

/** 
 * This abstract class is meant to be used to deal with 
 * many different inputed text commands by using polymorphism.
 */
public abstract class Command
{
	public final String regex;
	public final String detail;
			
	public Command (String regex, String detail)
	{
		this.regex = regex;
		this.detail = detail;
	}
	
	abstract void execute (String command);	
}