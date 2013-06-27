package swp_compiler_ss13.fuc.ir;

import java.util.Stack;

import org.apache.log4j.Logger;

public class WatchedStack<T> {

	private Logger logger = Logger.getLogger(WatchedStack.class);

	private Stack<T> stack;

	public WatchedStack() {
		this.stack = new Stack<T>();
	}

	public WatchedStack(Stack<T> stack) {
		this.stack = stack;
	}

	public T pop() {
		T e = stack.pop();
		logger.debug("pop " + e);
		return e;
	}

	public T push(T e) {
		logger.debug("push " + e);
		return stack.push(e);
	}

}

