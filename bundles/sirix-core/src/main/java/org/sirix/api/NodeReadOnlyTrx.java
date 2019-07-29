package org.sirix.api;

import java.time.Instant;
import org.brackit.xquery.atomic.QNm;
import org.sirix.access.trx.node.CommitCredentials;
import org.sirix.api.xml.XmlNodeReadOnlyTrx;
import org.sirix.exception.SirixException;
import org.sirix.node.Kind;


public interface NodeReadOnlyTrx extends AutoCloseable {

  /**
   * Get the transaction-ID of transaction.
   *
   * @return ID of the transaction
   */
  long getId();

  /**
   * Get the revision number of this transaction.
   *
   * @return immutable revision number of this transaction
   */
  int getRevisionNumber();

  /**
   * UNIX-style timestamp of the commit of the revision.
   *
   * @return instant, representing the milliseconds from unix epoch.
   */
  Instant getRevisionTimestamp();

  /**
   * Getting the maximum nodekey available in this revision.
   *
   * @return the maximum nodekey
   */
  long getMaxNodeKey();

  /**
   * Close shared read transaction and immediately release all resources.
   *
   * This is an idempotent operation and does nothing if the transaction is already closed.
   *
   * @throws SirixException if can't close {@link XmlNodeReadOnlyTrx}
   */
  @Override
  void close();

  /**
   * Get the node key of the currently selected node.
   *
   * @return node key of the currently selected node
   */
  long getNodeKey();


  /**
   * Get the {@link ResourceManager} this instance is bound to.
   *
   * @return the resource manager this transaction is bound to
   */
  ResourceManager<? extends NodeReadOnlyTrx, ? extends NodeTrx> getResourceManager();

  /**
   * Get the commit credentials.
   *
   * @return the commit credentials
   */
  CommitCredentials getCommitCredentials();

  /**
   * Move to a specific node.
   *
   * @param key the nodeKey of the node to move to
   * @return this transaction instance
   */
  Move<? extends NodeCursor> moveTo(long key);

  /**
   * Get the underlying page transaction.
   *
   * @return the underlying page transaction
   */
  PageReadOnlyTrx getPageTrx();

  /**
   * Get the path node key of the current node.
   *
   * @return the path node key
   */
  long getPathNodeKey();

  /**
   * Get key for given name. This is used for efficient name testing.
   *
   * @param name name, i.e., local part, URI, or prefix
   * @return internal key assigned to given name
   */
  int keyForName(String name);

  /**
   * Get name for key. This is used for efficient key testing.
   *
   * @param key key, i.e., local part key, URI key, or prefix key.
   * @return String containing name for given key
   */
  String nameForKey(int key);

  /**
   * Get the number of descendants of the current node.
   *
   * @return the number of descendants of the current node
   */
  long getDescendantCount();

  /**
   * Get the number of children of the current node.
   *
   * @return the number of children of the current node
   */
  long getChildCount();

  /**
   * Get the kind of path of the current node (only supported for path summary transactions).
   *
   * @return the kind of path of the current node
   */
  Kind getPathKind();

  /**
   * Determines if the current node is the document root node or not.
   *
   * @return {@code true}, if the current node is the document root node, {@code false} otherwise
   */
  boolean isDocumentRoot();

  /**
   * Determines if the state of the transaction has been changed to {@code closed}.
   *
   * @return {@code true}, if the transaction has been closed, {@code false} otherwise
   */
  boolean isClosed();

  /**
   * Get the name of the current node or {@code null}.
   *
   * @return the name of the current node or {@code null} if the node has no name
   */
  QNm getName();

  /**
   * Determines if the current node has children or not.
   *
   * @return {@code true}, if the current node has children, {@code false} otherwise
   */
  boolean hasChildren();

  long getHash();

  String getValue();
}
