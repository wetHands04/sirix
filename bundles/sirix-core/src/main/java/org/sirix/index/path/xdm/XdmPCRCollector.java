package org.sirix.index.path.xdm;

import java.util.Objects;
import java.util.Set;
import org.brackit.xquery.atomic.QNm;
import org.brackit.xquery.util.path.Path;
import org.brackit.xquery.util.path.PathException;
import org.sirix.api.NodeReadOnlyTrx;
import org.sirix.api.xdm.XdmNodeReadOnlyTrx;
import org.sirix.api.xdm.XdmNodeTrx;
import org.sirix.index.path.PCRCollector;
import org.sirix.index.path.PCRValue;
import org.sirix.index.path.summary.PathSummaryReader;
import org.sirix.utils.LogWrapper;
import org.slf4j.LoggerFactory;

public final class XdmPCRCollector implements PCRCollector {

  /** Logger. */
  private static final LogWrapper LOGGER =
      new LogWrapper(LoggerFactory.getLogger(XdmPCRCollector.class));

  private final NodeReadOnlyTrx mRtx;

  public XdmPCRCollector(final XdmNodeReadOnlyTrx rtx) {
    mRtx = Objects.requireNonNull(rtx, "The transaction must not be null.");
  }

  @Override
  public PCRValue getPCRsForPaths(Set<Path<QNm>> paths) {
    try (final PathSummaryReader reader = mRtx instanceof XdmNodeTrx
        ? ((XdmNodeTrx) mRtx).getPathSummary()
        : mRtx.getResourceManager().openPathSummary(mRtx.getRevisionNumber())) {
      final long maxPCR = reader.getMaxNodeKey();
      final Set<Long> pathClassRecords = reader.getPCRsForPaths(paths, true);
      return PCRValue.getInstance(maxPCR, pathClassRecords);
    } catch (final PathException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return PCRValue.getEmptyInstance();
  }
}