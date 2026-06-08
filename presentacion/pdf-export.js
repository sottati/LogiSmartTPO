/**
 * Exports LogiSmart Arquitectura.html to PDF using Chrome DevTools Protocol.
 * Run: node pdf-export.js
 * Output: LogiSmart Arquitectura.pdf (same folder)
 */

const { execFile, spawn } = require('child_process');
const http = require('http');
const path = require('path');
const fs = require('fs');

const CHROME = 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe';
const HTML_FILE = path.resolve(__dirname, 'LogiSmart Arquitectura.html');
const PDF_OUT   = path.resolve(__dirname, 'LogiSmart Arquitectura.pdf');
const PORT      = 9222;
const FILE_URL  = 'file:///' + HTML_FILE.replace(/\\/g, '/').replace(/ /g, '%20');

// Design size: 1920×1080 at 96 dpi → 20in × 11.25in
const PAGE_W_IN = 1920 / 96;
const PAGE_H_IN = 1080 / 96;

function get(url) {
  return new Promise((resolve, reject) => {
    http.get(url, res => {
      let data = '';
      res.on('data', d => data += d);
      res.on('end', () => { try { resolve(JSON.parse(data)); } catch(e) { resolve(data); } });
    }).on('error', reject);
  });
}

function send(ws, method, params = {}) {
  return new Promise((resolve, reject) => {
    const id = Math.floor(Math.random() * 1e9);
    const msg = JSON.stringify({ id, method, params });
    const callbacks = ws._callbacks || (ws._callbacks = new Map());
    callbacks.set(id, { resolve, reject });
    ws.send(msg);
  });
}

async function cdpConnect(wsUrl) {
  const { WebSocket } = await import('ws').catch(() => null) || {};
  if (!WebSocket) {
    // Fallback: use built-in net module for a minimal WS client
    return minimalWsClient(wsUrl);
  }
  return new Promise((resolve, reject) => {
    const ws = new WebSocket(wsUrl);
    ws.on('open', () => resolve(ws));
    ws.on('error', reject);
    ws.on('message', raw => {
      const msg = JSON.parse(raw);
      if (msg.id && ws._callbacks && ws._callbacks.has(msg.id)) {
        const cb = ws._callbacks.get(msg.id);
        ws._callbacks.delete(msg.id);
        if (msg.error) cb.reject(new Error(msg.error.message));
        else cb.resolve(msg.result);
      }
    });
  });
}

// Minimal WebSocket client using Node.js built-ins (no ws package needed)
function minimalWsClient(wsUrl) {
  return new Promise((resolve, reject) => {
    const net = require('net');
    const crypto = require('crypto');
    const url = new URL(wsUrl);
    const key = crypto.randomBytes(16).toString('base64');
    const sock = net.createConnection(parseInt(url.port) || 9222, url.hostname, () => {
      sock.write(
        `GET ${url.pathname + url.search} HTTP/1.1\r\n` +
        `Host: ${url.host}\r\n` +
        `Upgrade: websocket\r\n` +
        `Connection: Upgrade\r\n` +
        `Sec-WebSocket-Key: ${key}\r\n` +
        `Sec-WebSocket-Version: 13\r\n\r\n`
      );
    });
    let upgraded = false;
    let buf = Buffer.alloc(0);
    const callbacks = new Map();
    const ws = {
      _callbacks: callbacks,
      send(msg) {
        const data = Buffer.from(msg);
        const mask = crypto.randomBytes(4);
        const len = data.length;
        let header;
        if (len < 126) {
          header = Buffer.from([0x81, 0x80 | len, mask[0], mask[1], mask[2], mask[3]]);
        } else if (len < 65536) {
          header = Buffer.from([0x81, 0xFE, len >> 8, len & 0xFF, mask[0], mask[1], mask[2], mask[3]]);
        } else {
          header = Buffer.from([0x81, 0xFF, 0,0,0,0, (len>>24)&0xFF,(len>>16)&0xFF,(len>>8)&0xFF,len&0xFF, mask[0],mask[1],mask[2],mask[3]]);
        }
        const masked = Buffer.from(data.map((b,i) => b ^ mask[i%4]));
        sock.write(Buffer.concat([header, masked]));
      },
      close() { sock.destroy(); }
    };
    sock.on('data', chunk => {
      if (!upgraded) {
        buf = Buffer.concat([buf, chunk]);
        const end = buf.indexOf('\r\n\r\n');
        if (end === -1) return;
        upgraded = true;
        buf = buf.slice(end + 4);
        resolve(ws);
        if (!buf.length) return;
        chunk = buf; buf = Buffer.alloc(0);
      }
      buf = Buffer.concat([buf, chunk]);
      while (buf.length >= 2) {
        const fin = (buf[0] & 0x80) !== 0;
        const opcode = buf[0] & 0x0F;
        if (opcode === 8) { sock.destroy(); return; }
        let offset = 2;
        let payloadLen = buf[1] & 0x7F;
        if (payloadLen === 126) { if (buf.length < 4) break; payloadLen = (buf[2]<<8)|buf[3]; offset=4; }
        else if (payloadLen === 127) { if (buf.length < 10) break; payloadLen = Number(buf.readBigUInt64BE(2)); offset=10; }
        if (buf.length < offset + payloadLen) break;
        const payload = buf.slice(offset, offset + payloadLen);
        buf = buf.slice(offset + payloadLen);
        if (opcode === 1 || opcode === 2) {
          try {
            const msg = JSON.parse(payload.toString());
            if (msg.id && callbacks.has(msg.id)) {
              const cb = callbacks.get(msg.id); callbacks.delete(msg.id);
              if (msg.error) cb.reject(new Error(msg.error.message));
              else cb.resolve(msg.result);
            }
          } catch(e) {}
        }
      }
    });
    sock.on('error', reject);
  });
}

async function sleep(ms) { return new Promise(r => setTimeout(r, ms)); }

async function main() {
  console.log('Starting Chrome headless...');
  const chrome = spawn(CHROME, [
    '--headless=new',
    `--remote-debugging-port=${PORT}`,
    '--no-first-run',
    '--no-default-browser-check',
    '--disable-extensions',
    '--disable-background-networking',
    '--disable-sync',
    '--disable-translate',
    '--mute-audio',
    'about:blank',
  ], { stdio: 'ignore', detached: false });

  await sleep(2000);

  try {
    // Get list of targets
    const targets = await get(`http://localhost:${PORT}/json/list`);
    const target = targets.find(t => t.type === 'page') || targets[0];
    if (!target) throw new Error('No Chrome target found');

    console.log('Connecting to Chrome DevTools...');
    const ws = await cdpConnect(target.webSocketDebuggerUrl);

    // Enable Page domain
    await send(ws, 'Page.enable');

    console.log('Navigating to presentation...');
    await send(ws, 'Page.navigate', { url: FILE_URL });

    // Wait for page load + fonts (deck-stage waits up to 2s for fonts.ready)
    await sleep(4000);

    // Also wait for fonts explicitly
    await send(ws, 'Runtime.evaluate', {
      expression: 'document.fonts.ready',
      awaitPromise: true,
      timeout: 5000,
    });

    console.log('Generating PDF...');
    const result = await send(ws, 'Page.printToPDF', {
      paperWidth:  PAGE_W_IN,
      paperHeight: PAGE_H_IN,
      marginTop:    0,
      marginBottom: 0,
      marginLeft:   0,
      marginRight:  0,
      printBackground: true,
      preferCSSPageSize: true,
      scale: 1,
    });

    ws.close();

    fs.writeFileSync(PDF_OUT, Buffer.from(result.data, 'base64'));
    console.log(`PDF saved: ${PDF_OUT}`);
  } finally {
    chrome.kill();
  }
}

main().catch(e => { console.error(e); process.exit(1); });
