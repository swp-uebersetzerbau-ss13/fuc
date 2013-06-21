package swp_compiler_ss13.fuc.ast;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.binary.DoWhileNode;
import swp_compiler_ss13.common.ast.nodes.binary.LoopNode;
import swp_compiler_ss13.common.ast.nodes.binary.WhileNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BreakNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.StructIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

/**
 * @author kaworu
 * 
 */
public class ASTFactory {

	/**
	 * Stores the constructed AST
	 */
	AST ast;

	/**
	 * Stores the current node (is always a StatementNode which can have
	 * StatementNode children)
	 */
	ASTNode node;

	/**
	 * A Factory to create an AST without needing to write a million lines of
	 * code.
	 * 
	 * This is achieved by employing methods that act as "constructors" that get
	 * the child nodes directly and thus save the need to call the setters.
	 * 
	 * furthermore is the last StatementNode that can have additional
	 * StatementNodes as children remembered which allows easy appending to
	 * BlockNodes
	 */
	public ASTFactory() {
		this.ast = new ASTImpl();
		BlockNode program = new BlockNodeImpl();
		program.setSymbolTable(new SymbolTableImpl());
		this.ast.setRootNode(program);
		this.node = program;
	}

	/**
	 * returns the constructed ast
	 * 
	 * @return the constructed ast
	 */
	public AST getAST() {
		return this.ast;
	}

	/**
	 * returns the current node (is always a StatementNode which can have
	 * StatementNode children)
	 * 
	 * @return return the current node
	 */
	public ASTNode getNode() {
		return this.node;
	}

	/**
	 * moves the construction to the parent
	 * 
	 * @return the new current node
	 */
	public ASTNode goToParent() {
		ASTNode parent = this.node.getParentNode();
		assert (parent != null);
		this.node = parent;
		return this.node;
	}

	/**
	 * adds a declaration to the current node
	 * 
	 * @param identifier
	 *            the identifier to declare
	 * @param type
	 *            the type to declare
	 * @return the created DeclarationNode
	 */
	public DeclarationNode addDeclaration(String identifier, Type type) {
		DeclarationNode declaration = new DeclarationNodeImpl();
		BlockNode block = (BlockNode) this.node;
		block.addDeclaration(declaration);
		block.getSymbolTable().insert(identifier, type);
		declaration.setIdentifier(identifier);
		declaration.setType(type);
		declaration.setParentNode(this.node);
		return declaration;
	}

	/**
	 * creates a new BlockNode, adds it as child to the current node, and sets
	 * it as the new current node
	 * 
	 * @return the created BlockNode
	 */
	public BlockNode addBlock() {
		BlockNode block = new BlockNodeImpl();
		block.setParentNode(this.node);

		ASTNode parentBlock = this.node;
		while (!(parentBlock instanceof BlockNode)) {
			parentBlock = parentBlock.getParentNode();
		}

		block.setSymbolTable(new SymbolTableImpl(((BlockNode) parentBlock).getSymbolTable()));
		switch (this.node.getNodeType()) {
		case BranchNode:
			BranchNode branch = ((BranchNode) this.node);
			if (branch.getStatementNodeOnTrue() == null) {
				branch.setStatementNodeOnTrue(block);
			} else if ((branch.getStatementNodeOnFalse() == null)) {
				branch.setStatementNodeOnFalse(block);
			} else {
				assert (false);
			}
			break;
		case DoWhileNode:
		case WhileNode:
			((LoopNode) this.node).setLoopBody(block);
			break;
		case BlockNode:
			((BlockNode) this.node).addStatement(block);
			break;
		default:
			assert (false);
			break;
		}
		this.node = block;
		return block;
	}

	/**
	 * creates a new ReturnNode and adds it as child to the current node
	 * 
	 * @param identifier
	 *            the identifier to return
	 * @return the created ReturnNode
	 */
	public ReturnNode addReturn(IdentifierNode identifier) {
		ReturnNode ret = new ReturnNodeImpl();
		((BlockNode) this.node).addStatement(ret);
		ret.setParentNode(this.node);
		ret.setRightValue(identifier);
		if (identifier != null) {
			identifier.setParentNode(ret);
		}
		return ret;
	}

	/**
	 * creates a new BranchNode, adds it as child to the current node, and sets
	 * it as the new current node
	 * 
	 * the next addBlock() will add the TrueBlock. goToParent(); addBlock() will
	 * add the FalseBlock
	 * 
	 * @param condition
	 *            the condition which decides which childBlock gets executed
	 * @return the created BranchNode
	 */
	public BranchNode addBranch(ExpressionNode condition) {
		BranchNode branch = new BranchNodeImpl();
		((BlockNode) this.node).addStatement(branch);
		branch.setParentNode(this.node);
		branch.setCondition(condition);
		condition.setParentNode(branch);
		this.node = branch;
		return branch;
	}

	/**
	 * creates a new LoopNode, adds it as child to the current node, and sets it
	 * as the new current node
	 * 
	 * (needs 2 calls to goToParent() to get to the original node)
	 * 
	 * @param condition
	 *            the condition to end the loop
	 * @param loop
	 *            the type of loop this is (while, dowhile)
	 * @return the created LoopNode
	 */
	protected LoopNode addLoop(ExpressionNode condition, LoopNode loop) {
		((BlockNode) this.node).addStatement(loop);
		loop.setParentNode(this.node);
		loop.setCondition(condition);
		condition.setParentNode(loop);
		this.node = loop;
		this.addBlock();
		return loop;
	}

	/**
	 * creates a new DoWhileNode, adds it as child to the current node, and sets
	 * it as the new current node
	 * 
	 * (needs 2 calls to goToParent() to get to the original node)
	 * 
	 * @param condition
	 *            the condition to end the loop
	 * @return the created DoWhileNode
	 */
	public DoWhileNode addDoWhile(ExpressionNode condition) {
		return (DoWhileNode) this.addLoop(condition, new DoWhileNodeImpl());
	}

	/**
	 * creates a new WhileNode, adds it as child to the current node, and sets
	 * it as the new current node
	 * 
	 * (needs 2 calls to goToParent() to get to the original node)
	 * 
	 * @param condition
	 *            the condition to end the loop
	 * @return the created WhileNode
	 */
	public WhileNode addWhile(ExpressionNode condition) {
		return (WhileNode) this.addLoop(condition, new WhileNodeImpl());
	}

	/**
	 * creates a new addPrint and adds it as child to the current node
	 * 
	 * @param identifier
	 *            the identifier to print
	 * @return the created PrintNode
	 */
	public PrintNode addPrint(IdentifierNode identifier) {
		PrintNodeImpl print = new PrintNodeImpl();
		((BlockNode) this.node).addStatement(print);
		print.setParentNode(this.node);
		print.setRightValue(identifier);
		identifier.setParentNode(print);
		return print;
	}

	/**
	 * creates a new BreakNode and adds it as child to the current node
	 * 
	 * @return the created BreakNode
	 */
	public BreakNode addBreak() {
		BreakNodeImpl breaknode = new BreakNodeImpl();
		((BlockNode) this.node).addStatement(breaknode);
		breaknode.setParentNode(this.node);
		return breaknode;
	}

	/**
	 * adds an expression as child to the current node
	 * 
	 * @param expression
	 *            the expression to add to the current node
	 * @return the added ExpressionNode
	 */
	public ExpressionNode addExpression(ExpressionNode expression) {
		((BlockNode) this.node).addStatement(expression);
		expression.setParentNode(this.node);
		return expression;
	}

	/**
	 * creates a new BinaryExpressionNode and sets the relations to the children
	 * 
	 * @param operator
	 *            the BinaryOperator
	 * @param leftExpression
	 *            the left expression
	 * @param rightExpression
	 *            the right expression
	 * @return the created BinaryExpressionNode
	 */
	public BinaryExpressionNode newBinaryExpression(BinaryOperator operator, ExpressionNode leftExpression,
			ExpressionNode rightExpression) {
		BinaryExpressionNode binaryExpression = null;
		switch (operator) {
		case ADDITION:
		case DIVISION:
		case MULTIPLICATION:
		case SUBSTRACTION:
			binaryExpression = new ArithmeticBinaryExpressionNodeImpl();
			break;
		case EQUAL:
		case GREATERTHAN:
		case GREATERTHANEQUAL:
		case INEQUAL:
		case LESSTHAN:
		case LESSTHANEQUAL:
			binaryExpression = new RelationExpressionNodeImpl();
			break;
		case LOGICAL_AND:
		case LOGICAL_OR:
			binaryExpression = new LogicBinaryExpressionNodeImpl();
			break;
		default:
			assert (false);
			break;
		}
		binaryExpression.setOperator(operator);
		binaryExpression.setLeftValue(leftExpression);
		leftExpression.setParentNode(binaryExpression);
		binaryExpression.setRightValue(rightExpression);
		rightExpression.setParentNode(binaryExpression);
		return binaryExpression;
	}
	
	/**
	 * Creates a new {@link PrintNode} with the given {@link IdentifierNode} as right value.
	 * 
	 * @param id May be <code>null</code>
	 * @return
	 */
	public PrintNode newPrint(IdentifierNode id) {
	   PrintNode print = new PrintNodeImpl();
	   if (id == null) {
	      print.setRightValue(null);
	   } else {
	      id.setParentNode(print);
         print.setRightValue(id);
	   }
	   return print;
	}

	/**
	 * creates a new UnaryExpressionNode and sets the relations to the children
	 * 
	 * @param operator
	 *            the UnaryOperator
	 * @param expression
	 *            the expression
	 * @return the created UnaryExpressionNode
	 */
	public UnaryExpressionNode newUnaryExpression(UnaryOperator operator, ExpressionNode expression) {
		UnaryExpressionNode unaryExpression = null;
		switch (operator) {
		case LOGICAL_NEGATE:
			unaryExpression = new LogicUnaryExpressionNodeImpl();
			break;
		case MINUS:
			unaryExpression = new ArithmeticUnaryExpressionNodeImpl();
			break;
		default:
			assert (false);
			break;
		}
		unaryExpression.setOperator(operator);
		unaryExpression.setRightValue(expression);
		expression.setParentNode(unaryExpression);
		return unaryExpression;
	}

	/**
	 * creates a literal
	 * 
	 * @param literal
	 *            the name of the literal
	 * @param type
	 *            the type of the literal
	 * @return the created LiteralNode
	 */
	public LiteralNode newLiteral(String literal, Type type) {
		LiteralNode literalNode = new LiteralNodeImpl();
		literalNode.setLiteral(literal);
		literalNode.setLiteralType(type);
		return literalNode;
	}

	/**
	 * creates a AssignmentNode and sets the relations to the children
	 * 
	 * @param identifier
	 *            the identifier to be assigned
	 * @param expression
	 *            the expression which is assigned
	 * @return the created AssignmentNode
	 */
	public AssignmentNode newAssignment(IdentifierNode identifier, ExpressionNode expression) {
		AssignmentNode assignment = new AssignmentNodeImpl();
		identifier.setParentNode(assignment);
		expression.setParentNode(assignment);
		assignment.setLeftValue(identifier);
		assignment.setRightValue(expression);
		return assignment;
	}

	/**
	 * creates a AssignmentNode and adds it to the current node
	 * 
	 * @param identifier
	 *            the identifier to be assigned
	 * @param expression
	 *            the expression which is assigned
	 * @return the created AssignmentNode
	 */
	public AssignmentNode addAssignment(IdentifierNode identifier, ExpressionNode expression) {
		return (AssignmentNode) this.addExpression(this.newAssignment(identifier, expression));
	}

	/**
	 * creates a BasicIdentifierNode
	 * 
	 * @param identifier
	 *            the name of the identifier
	 * @return the created BasicIdentifierNode
	 */
	public BasicIdentifierNode newBasicIdentifier(String identifier) {
		BasicIdentifierNode basicIdentifier = new BasicIdentifierNodeImpl();
		basicIdentifier.setIdentifier(identifier);
		return basicIdentifier;
	}

	/**
	 * creates a ArrayIdentifierNode
	 * 
	 * @param index
	 *            the index of the identifier in the array
	 * @param identifier
	 *            the IdentifierNode in the array
	 * @return the created ArrayIdentifierNode
	 */
	public ArrayIdentifierNode newArrayIdentifier(Integer index, IdentifierNode identifier) {
		ArrayIdentifierNode arrayIdentifier = new ArrayIdentifierNodeImpl();
		arrayIdentifier.setIndex(index);
		arrayIdentifier.setIdentifierNode(identifier);
		identifier.setParentNode(arrayIdentifier);
		return arrayIdentifier;
	}

	/**
	 * creates a StructIdentifierNode
	 * 
	 * @param fieldName
	 *            the fieldName of the identifier in the struct
	 * @param identifier
	 *            the IdentifierNode in the struct
	 * @return the created StructIdentifierNode
	 */
	public StructIdentifierNode newStructIdentifier(String fieldName, IdentifierNode identifier) {
		StructIdentifierNode structIdentifier = new StructIdentifierNodeImpl();
		structIdentifier.setFieldName(fieldName);
		structIdentifier.setIdentifierNode(identifier);
		identifier.setParentNode(structIdentifier);
		return structIdentifier;
	}

}