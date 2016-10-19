/**
 * Note this test needs a serial loopback
 * socat works well for me
 *
 * socat -d -d pty,raw,echo=0 pty,raw,echo=0
 * or
 * socat PTY,link=/dev/ttyS10 PTY,link=/dev/ttyS11
 */
package org.swamps.houseController.serialInterface;