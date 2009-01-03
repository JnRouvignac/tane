package com.stateofflow.eclipse.tane.util;

import static org.eclipse.jdt.internal.corext.dom.NodeFinder.*;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Name;

public class ASTUtils {
    public static ASTNode findNode(final CompilationUnit root, final int offset) {
        return findNode(root, offset, 0);
    }

    public static ASTNode findNode(final CompilationUnit root, final int offset, final int length) {
        return perform(root, offset, length);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ASTNode> T getAncestor(ASTNode node, final Class<T> parentClass) {
    	return node == null || parentClass.isInstance(node) ? (T) node : getAncestor(node.getParent(), parentClass);
    }

    public static ASTNode getDeclarationForType(final CompilationUnit root, final IType type) throws JavaModelException {
        final Name result = (Name) findNode(root, type.getNameRange().getOffset());
        return type.isAnonymous() ? getAncestor(result, AnonymousClassDeclaration.class) : getAncestor(result, AbstractTypeDeclaration.class);
    }

    public static ChildListPropertyDescriptor getBodyDeclarationPropertyForType(final CompilationUnit root, final IType type) throws JavaModelException {
        final ASTNode result = getDeclarationForType(root, type);
        if (result instanceof AbstractTypeDeclaration) {
            return ((AbstractTypeDeclaration) result).getBodyDeclarationsProperty();
        } else if (result instanceof AnonymousClassDeclaration) {
            return AnonymousClassDeclaration.BODY_DECLARATIONS_PROPERTY;
        }

        Assert.isTrue(false);
        return null;
    }
}
